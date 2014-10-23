package es;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

//import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertAcked;

//import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.node.Node;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.IndicesAdminClient;

import org.elasticsearch.cluster.metadata.MappingMetaData;

import org.elasticsearch.common.hppc.cursors.ObjectObjectCursor;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;

import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.index.IndexRequest;

import org.elasticsearch.search.SearchHit;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import java.util.concurrent.TimeUnit;
import java.util.Map;

public class EsTest {
    private static final Logger log = LoggerFactory.getLogger(EsTest.class);

    private static final String FIELD_ID = "_id_document";


    private void waitForCluster(ClusterAdminClient cac, String indexName) {
        cac.prepareHealth(indexName).setWaitForYellowStatus().execute().actionGet();
    }

    private void deleteIndexIfExists(IndicesAdminClient iac, String indexName) {
        if (iac.prepareExists(indexName).execute().actionGet().isExists()) {
            boolean ack = iac.prepareDelete(indexName).execute().actionGet().isAcknowledged();
            log.info("index {} deleted: {}", indexName, Boolean.toString(ack));
        }
    }
/*
    private void createIndex(IndicesAdminClient iac, long projectId) {
        String projectIdStr = Long.toString(projectId);
        String indexName = projectIdStr = "_cur";
        String aliasRead = "read_" + projectIdStr;
        String aliasWrite = "write_" + projectIdStr;

        deleteIndexIfExists(iac, indexName);

        boolean ack = iac.prepareCreate(indexName)
            .setSettings(settingsBuilder().put("number_of_shards", Integer.toString(2)))
            .addAlias(new Alias(aliasRead)).addAlias(new Alias(aliasWrite))
            .get().isAcknowledged();

        log.info("index {} created: {}", indexName, Boolean.toString(ack));
    }
*/
    // look for auto-filling stuff:
    // https://github.com/elasticsearch/elasticsearch/blob/master/src/test/java/org/elasticsearch/search/scroll/SearchScrollTests.java

    private CreateIndexRequestBuilder addIndexMappings(IndicesAdminClient iac, CreateIndexRequestBuilder rb, String indexName) throws IOException {
        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> indicesToMappings =
            iac.prepareGetMappings(indexName).get().mappings();
        ImmutableOpenMap<String, MappingMetaData> indexMappings = indicesToMappings.get(indexName);

        for (ObjectObjectCursor<String, MappingMetaData> c : indexMappings) {
            //log.info("mapping for type: {}", c.key);
            Map<String, Object> typeMappingsMap = c.value.sourceAsMap();
            //for (String k : typeMappingsMap.keySet()) { log.info("mapping for: {}", k); }
            rb = rb.addMapping(c.key, typeMappingsMap);
        }
        return rb;
    }

    private CreateIndexRequestBuilder addIndexSettings(IndicesAdminClient iac, CreateIndexRequestBuilder rb, String indexName, int shards) {
        ImmutableOpenMap<String, Settings> indexToSettings = iac.prepareGetSettings(indexName).get().getIndexToSettings();
        Settings indexSettings = indexToSettings.get(indexName);

        Map<String, String> indexSettingsMap = indexSettings.getAsMap(); // getAsStructuredMap();
        //for (String k : indexSettingsMap.keySet()) { log.info("settings for: {}", k); }
        return rb.setSettings(
            // Note: "index.uuid" is auto-generated as a new one
            settingsBuilder()
                .put(indexSettings)
                .put("number_of_shards", Integer.toString(shards)
            )
        );
    }

    private void allDocsToBulk(Client client, BulkProcessor bp, String srcIndexName, String dstIndexName, int batchSize) {
        SearchResponse resp = client.prepareSearch(srcIndexName)
            .setSearchType(SearchType.SCAN)
            .setScroll(TimeValue.timeValueMinutes(2))
            .setQuery(matchAllQuery())
            .addFieldDataField(FIELD_ID)
            .setSize(batchSize/5) // TODO: why do we divide here???
            .execute().actionGet();
        do {
            for (SearchHit hit : resp.getHits()) {
                String hitType = hit.getType();
                log.debug("hit type: {}, id: {}", hitType, hit.getId()); // ", srcRef: {},  hit.getSourceRef().toUtf8()

                IndexRequest ir = new IndexRequest(dstIndexName)
                    .source(hit.getSourceRef(), true)
                    .type(hitType)
                    .id(hit.getId());

                final boolean useRouting = true;
                if (useRouting) {
                    Long valIdDoc = hit.field("_id_document").value(); // in-place just use: .<Long>value();
                    log.debug("hit _id_document: {}", valIdDoc);

                    if ("verbatim".equals(hitType) || "sentence".equals(hitType)) {
                        ir.routing(valIdDoc.toString());
                    }
                }

                bp.add(ir);
            }
            resp = client.prepareSearchScroll(resp.getScrollId())
                .setScroll(TimeValue.timeValueMinutes(10))
                .execute().actionGet();
        }
        while (resp.getHits().hits().length > 0);
    }

    private void migrateIndex(Client client, long projectId, int batchSize, int writeThreads) throws IOException {
        String projectIdStr = Long.toString(projectId);
        String srcIndexName = projectIdStr + "_cur";
        String dstIndexName = projectIdStr + "_0";

        IndicesAdminClient iac = client.admin().indices();

        deleteIndexIfExists(iac, dstIndexName);

        CreateIndexRequestBuilder rb = iac.prepareCreate(dstIndexName);
        rb = addIndexMappings(iac, rb, srcIndexName);
        rb = addIndexSettings(iac, rb, srcIndexName, 5);
        assertTrue(rb.get().isAcknowledged());

        iac.prepareRefresh(srcIndexName).execute().actionGet();

        waitForCluster(client.admin().cluster(), srcIndexName);

        try (IndexLocker il = new IndexLocker(iac, srcIndexName);
            BulkWaiter bw = new BulkWaiter();
            BulkProcessor bp = BulkProcessor.builder(client, bw)
                .setBulkActions(batchSize) 
                .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB)) 
                .setConcurrentRequests(writeThreads) 
                //.setConcurrentRequests(0)
                .build();
        ) {
            allDocsToBulk(client, bp, srcIndexName, dstIndexName, batchSize);
        }

        // alias stuff
        // assertTrue(client.admin().indices().prepareAliases().addAlias("0", "read_2").execute().actionGet().isAcknowledged());
    }
            
    @Test
    public void testMigrateIndex() throws Exception {
        final String clusterName = "epbygomw0024-5432-postgres-win_ss";
        //final String clusterName = "elasticsearch";
        final long projectId = 1404;
        //final long projectId = 1;

        Settings settings = settingsBuilder()
            .put("discovery.zen.ping.unicast.hosts", "localhost")
            .put("discovery.zen.ping.multicast.enabled", false)
            .put("node.master", false)
            .build();

        Node node = null;
        try {
            node = nodeBuilder().clusterName(clusterName).client(true).settings(settings).node(); // data(false).
            Client client = node.client();

            //IndicesAdminClient iac = client.admin().indices();
            //for (long i = 0; i < 2; ++i) {
            //    createIndex(iac, i);
            //} 

            migrateIndex(client, projectId, 1000, 4);
        } finally {
            try {
                if (node != null) {
                    node.close();
                }
            } catch (Throwable t) {
            }
        }
    }
}
