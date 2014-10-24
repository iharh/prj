package es;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.ClusterAdminClient;
import org.elasticsearch.client.IndicesAdminClient;

import org.elasticsearch.cluster.metadata.MappingMetaData;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.hppc.cursors.ObjectObjectCursor;
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
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import java.util.concurrent.TimeUnit;
import java.util.Map;

class IndexMigrator {
    private static final Logger log = LoggerFactory.getLogger(IndexMigrator.class);

    private static final String FIELD_ID = "_id_document";


    private Client client;
    private IndicesAdminClient iac;
    private ClusterAdminClient cac;

    public IndexMigrator(Client client) {
        this.client = client;
        iac = client.admin().indices();
        cac = client.admin().cluster();
    }

    private void createIndexCopyMetadata(String srcIndexName, String dstIndexName, int shards) throws IOException {
        deleteIndexIfExists(dstIndexName);

        CreateIndexRequestBuilder rb = iac.prepareCreate(dstIndexName);
        rb = addIndexMappings(rb, srcIndexName);
        rb = addIndexSettings(rb, srcIndexName, shards);
        boolean ack = rb.get().isAcknowledged();
        log.info("index {} created: {}", dstIndexName, Boolean.toString(ack));
    }

    public void migrateIndex(long projectId, int shards, int batchSize, int writeThreads) throws IOException {
        String projectIdStr = Long.toString(projectId);
        String srcIndexName = projectIdStr + "_cur";
        String dstIndexName = projectIdStr + "_0";

        createIndexCopyMetadata(srcIndexName, dstIndexName, shards);

        iac.prepareRefresh(srcIndexName).execute().actionGet();

        waitForCluster(srcIndexName);

        try (IndexLocker il = new IndexLocker(iac, srcIndexName);
            BulkWaiter bw = new BulkWaiter();
            BulkProcessor bp = BulkProcessor.builder(client, bw)
                .setBulkActions(batchSize) 
                .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB)) 
                .setConcurrentRequests(writeThreads) 
                //.setConcurrentRequests(0)
                .build();
        ) {
            allDocsToBulk(bp, srcIndexName, dstIndexName, batchSize);
        }

        // alias stuff
        // assertTrue(client.admin().indices().prepareAliases().addAlias("0", "read_2").execute().actionGet().isAcknowledged());
    }


    private String getRoutingValue(SearchHit hit) {
        String hitType = hit.getType();

        Long valIdDoc = hit.field(FIELD_ID).value(); // in-place just use: .<Long>value();
        log.debug("hit {}: {}", FIELD_ID, valIdDoc);

        if ("verbatim".equals(hitType) || "sentence".equals(hitType)) {
            return valIdDoc.toString();
        }
        return null;
    }

    private void allDocsToBulk(BulkProcessor bp, String srcIndexName, String dstIndexName, int batchSize) {
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

                String routingVal = getRoutingValue(hit);
                if (routingVal != null) {
                    ir.routing(routingVal);
                }

                bp.add(ir);
            }
            resp = client.prepareSearchScroll(resp.getScrollId())
                .setScroll(TimeValue.timeValueMinutes(10))
                .execute().actionGet();
        }
        while (resp.getHits().hits().length > 0);
    }

    private CreateIndexRequestBuilder addIndexMappings(CreateIndexRequestBuilder rb, String indexName) throws IOException {
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

    private CreateIndexRequestBuilder addIndexSettings(CreateIndexRequestBuilder rb, String indexName, int shards) {
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

    private void waitForCluster(String indexName) {
        cac.prepareHealth(indexName).setWaitForYellowStatus().execute().actionGet();
    }

    private void deleteIndexIfExists(String indexName) {
        if (iac.prepareExists(indexName).execute().actionGet().isExists()) {
            boolean ack = iac.prepareDelete(indexName).execute().actionGet().isAcknowledged();
            log.info("index {} deleted: {}", indexName, Boolean.toString(ack));
        }
    }
}
