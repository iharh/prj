package com.clarabridge.elasticsearch.ingex.migrate;

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

import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.search.SearchRequestBuilder;
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

public class IndexMigrator {
    private static final Logger log = LoggerFactory.getLogger(IndexMigrator.class);

    public static final String INDEX_SETTING_NUMBER_OF_SHARDS = "number_of_shards"; //$NON-NLS-1$


    private Client client;
    private IndicesAdminClient iac;
    private ClusterAdminClient cac;
    private IndexNameFinder inf;

    public IndexMigrator(Client client) {
        this.client = client;
        cac = client.admin().cluster();
        iac = client.admin().indices();
        inf = new IndexNameFinder(iac);
    }

    public void migrateIndex(long projectId, int shards, int batchSize, int writeThreads) throws IOException {
        String projectIdStr = Long.toString(projectId);
        String srcIndexName = inf.findCur(projectIdStr);
        String dstIndexName = inf.findNext(projectIdStr);

        createIndexCopyMetadata(srcIndexName, dstIndexName, shards);

        iac.prepareRefresh(srcIndexName).get();

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

        String readAliasName = inf.findReadAlias(projectIdStr);
        boolean ack_read = iac.prepareAliases().addAlias(dstIndexName, readAliasName).removeAlias(srcIndexName, readAliasName).get().isAcknowledged();
        log.info("alias {} created: {}", readAliasName, Boolean.toString(ack_read));

        String writeAliasName = inf.findWriteAlias(projectIdStr);
        boolean ack_write = iac.prepareAliases().addAlias(dstIndexName, writeAliasName).removeAlias(srcIndexName, writeAliasName).get().isAcknowledged();
        log.info("alias {} created: {}", readAliasName, Boolean.toString(ack_write));
    }

    private void createIndexCopyMetadata(String srcIndexName, String dstIndexName, int shards) throws IOException {
        deleteIndexIfExists(dstIndexName);

        CreateIndexRequestBuilder rb = iac.prepareCreate(dstIndexName);
        rb = addIndexMappings(rb, srcIndexName);
        rb = addIndexSettings(rb, srcIndexName, shards);
        boolean ack = rb.get().isAcknowledged();
        log.info("index {} created: {}", dstIndexName, Boolean.toString(ack));
    }

    private void allDocsToBulk(BulkProcessor bp, String srcIndexName, String dstIndexName, int batchSize) {
        SearchRequestBuilder srb = client.prepareSearch(srcIndexName)
            .setSearchType(SearchType.SCAN)
            .setScroll(TimeValue.timeValueMinutes(2))
            .setQuery(matchAllQuery())
            .setSize(batchSize/5) // TODO: why do we divide here???
        ;
        for (int i = 0; i < ClbRoutingFinder.usedESFieldNames.length; ++i) {
            srb.addFieldDataField(ClbRoutingFinder.usedESFieldNames[i]);
        }
        SearchResponse resp = srb.get();
        do {
            for (SearchHit hit : resp.getHits()) {
                String hitType = hit.getType();
                log.debug("hit type: {}, id: {}", hitType, hit.getId()); // ", srcRef: {},  hit.getSourceRef().toUtf8()

                IndexRequest ir = new IndexRequest(dstIndexName)
                    .source(hit.getSourceRef(), true)
                    .type(hitType)
                    .id(hit.getId());

                String routingVal = ClbRoutingFinder.getRoutingValue(hit);
                if (routingVal != null) {
                    ir.routing(routingVal);
                }

                bp.add(ir);
            }
            resp = client.prepareSearchScroll(resp.getScrollId())
                .setScroll(TimeValue.timeValueMinutes(10))
                .get();
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
                .put(INDEX_SETTING_NUMBER_OF_SHARDS, Integer.toString(shards)
            )
        );
    }

    private void waitForCluster(String indexName) {
        cac.prepareHealth(indexName).setWaitForYellowStatus().get();
    }

    private void deleteIndexIfExists(String indexName) {
        if (iac.prepareExists(indexName).get().isExists()) {
            boolean ack = iac.prepareDelete(indexName).get().isAcknowledged();
            log.info("index {} deleted: {}", indexName, Boolean.toString(ack));
        }
    }
}
