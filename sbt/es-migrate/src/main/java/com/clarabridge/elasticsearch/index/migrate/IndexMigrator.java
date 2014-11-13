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
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.index.IndexRequest;

import org.elasticsearch.index.fielddata.FieldDataType;
import org.elasticsearch.index.mapper.core.StringFieldMapper;

import org.elasticsearch.search.SearchHit;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IndexMigrator {
    private static final Logger log = LoggerFactory.getLogger(IndexMigrator.class);

    public static final String INDEX_SETTING_NUMBER_OF_SHARDS = "number_of_shards"; //$NON-NLS-1$

    public static final String TYPE_PERCOLATOR = "percolator_mapping"; //$NON-NLS-1$
    public static final String TYPE_CLB_META = "clb_meta"; //$NON-NLS-1$
    public static final String OBJ_META = "_meta"; //$NON-NLS-1$
    public static final String FIELD_ENABLED = "enabled"; //$NON-NLS-1$
    public static final String MAPPING_PROPERTIES = "properties"; //$NON-NLS-1$

    public static final String FIELDDATA = "fielddata"; //$NON-NLS-1$

    private static final String ERR_INVALID_GEN = "Invalid generation: %s"; //$NON-NLS-1$


    private Client client;
    private IndicesAdminClient iac;
    private ClusterAdminClient cac;
    private IndexNameFinder inf;
    private IndexEnabler ien;

    public IndexMigrator(Client client) {
        this.client = client;
        cac = client.admin().cluster();
        iac = client.admin().indices();
        inf = new IndexNameFinder(iac);
        ien = new IndexEnabler(iac);
    }

    /*
    public void checkIndexAliases(long projectId) {
        String projectIdStr = Long.toString(projectId);
        String srcIndexName = inf.findCur(projectIdStr);

        String readAliasName = inf.findReadAlias(projectIdStr);
        if (!iac.prepareAliasesExist(readAliasName).get().exists()) {
            boolean ack_read = iac.prepareAliases().addAlias(srcIndexName, readAliasName).get().isAcknowledged();
            log.info("alias {} created: {}", readAliasName, Boolean.toString(ack_read));
        }

        String writeAliasName = inf.findWriteAlias(projectIdStr);
        if (!iac.prepareAliasesExist(writeAliasName).get().exists()) {
            boolean ack_read = iac.prepareAliases().addAlias(srcIndexName, writeAliasName).get().isAcknowledged();
            log.info("alias {} created: {}", writeAliasName, Boolean.toString(ack_read));
        }
    }
    */

    public void switchIndex(long projectId, long generation) throws IOException {
        String projectIdStr = Long.toString(projectId);
        String srcIndexName = inf.findCur(projectIdStr);
        String dstIndexName = inf.findSpecific(projectIdStr, generation);
        if (dstIndexName == null) {
            throw new IllegalArgumentException(String.format(ERR_INVALID_GEN, generation));
        }
        log.info("switching index: {} to index: {}", srcIndexName, dstIndexName);

        if (!srcIndexName.equals(dstIndexName)) {
            switchAliasesAndEnabling(projectIdStr, srcIndexName, dstIndexName);
        }
    }

    private void refreshIndex(String indexName) {
    }

    public void migrateIndex(long projectId, int shards, int batchSize, int writeThreads, Set<String> dvFields, boolean obsolete) throws IOException {
        String projectIdStr = Long.toString(projectId);
        String srcIndexName = inf.findCur(projectIdStr);
        String dstIndexName = obsolete ? inf.findObsolete(projectIdStr) : inf.findNext(projectIdStr);
        log.info("migrating index: {} to index: {}", srcIndexName, dstIndexName);

        createIndexCopyMetadata(srcIndexName, dstIndexName, shards, dvFields);

        refreshIndex(srcIndexName);
        waitForClusterAndRefresh(srcIndexName);

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

        refreshIndex(dstIndexName);
        waitForClusterAndRefresh(dstIndexName);

        switchAliasesAndEnabling(projectIdStr, srcIndexName, dstIndexName);
    }


    private void createIndexCopyMetadata(String srcIndexName, String dstIndexName, int shards, Set<String> dvFields) throws IOException {
        deleteIndexIfExists(dstIndexName);

        CreateIndexRequestBuilder rb = iac.prepareCreate(dstIndexName);
        rb = addIndexMappings(rb, srcIndexName, dvFields);
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

    private CreateIndexRequestBuilder addIndexMappings(CreateIndexRequestBuilder rb, String indexName, Set<String> dvFields) throws IOException {
        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> indicesToMappings =
            iac.prepareGetMappings(indexName).get().mappings();
        ImmutableOpenMap<String, MappingMetaData> indexMappings = indicesToMappings.get(indexName);

        Map<String, Object> enableDocValue = new HashMap<String, Object>(1);
        enableDocValue.put(FieldDataType.FORMAT_KEY, FieldDataType.DOC_VALUES_FORMAT_VALUE);

        for (ObjectObjectCursor<String, MappingMetaData> c : indexMappings) {
            String mappingType = c.key;
            log.debug("mapping for type: {}", mappingType);
            Map<String, Object> typeMappingsMap = c.value.sourceAsMap();
            if (dvFields != null /*&& !TYPE_PERCOLATOR.equals(mappingType)*/) {
                //for (String k : typeMappingsMap.keySet()) { log.debug("mapping for: {}", k); }
                @SuppressWarnings("unchecked")
                Map<String, Object> propertiesMap = (Map<String, Object>)typeMappingsMap.get(MAPPING_PROPERTIES);
                //for (String k : propertiesMap.keySet()) { log.debug("mapping properties for: {}", k); }

                for(Map.Entry<String, Object> entry : propertiesMap.entrySet()) {
                    String fieldName = entry.getKey();
                    if (dvFields.contains(fieldName)) {
                        @SuppressWarnings("unchecked")
                        Map<String, Object> fieldValueMap = (Map<String, Object>)entry.getValue();

                        String type = (String)fieldValueMap.get("type"); //$NON-NLS-1$
                        String index = (String)fieldValueMap.get("index"); //$NON-NLS-1$
                        String analyzer = (String)fieldValueMap.get("analyzer"); //$NON-NLS-1$
                        String index_analyzer = (String)fieldValueMap.get("index_analyzer"); //$NON-NLS-1$

                        boolean isTypeString = StringUtils.isBlank(type) || StringFieldMapper.CONTENT_TYPE.equals(type);
                        boolean isAnalyzed = !(StringUtils.isBlank(analyzer) && StringUtils.isBlank(index_analyzer)) ||
                            StringUtils.isBlank(index) || "analyzed".equals(index); //$NON-NLS-1$

                        if (isTypeString && isAnalyzed) {
                            log.warn("Can not set a docValue for analyzed string field: {}", fieldName);
                        } else {
                            log.info("Set a docValue for field: {}", fieldName);
                            fieldValueMap.put(FIELDDATA, enableDocValue); //$NON-NLS-1$
                        }
                    }
                }
            }
            rb = rb.addMapping(c.key, typeMappingsMap);
        }
        return rb;
    }

    private CreateIndexRequestBuilder addIndexSettings(CreateIndexRequestBuilder rb, String indexName, int shards) {
        ImmutableOpenMap<String, Settings> indexToSettings = iac.prepareGetSettings(indexName).get().getIndexToSettings();
        Settings indexSettings = indexToSettings.get(indexName);

        Map<String, String> indexSettingsMap = indexSettings.getAsMap(); // getAsStructuredMap();

        for (String k : indexSettingsMap.keySet()) {
            log.debug("settings for: {}", k);
        }

        return rb.setSettings(
            // Note: "index.uuid" is auto-generated as a new one
            settingsBuilder()
                .put(indexSettings)
                .put(INDEX_SETTING_NUMBER_OF_SHARDS, Integer.toString(shards)
            )
        );
    }

    private void waitForClusterAndRefresh(String indexName) {
        cac.prepareHealth(indexName).setWaitForYellowStatus().get();
        iac.prepareRefresh(indexName).get();
    }

    private void deleteIndexIfExists(String indexName) {
        if (iac.prepareExists(indexName).get().isExists()) {
            boolean ack = iac.prepareDelete(indexName).get().isAcknowledged();
            log.info("index {} deleted: {}", indexName, Boolean.toString(ack));
        }
    }

    private void switchAliasesAndEnabling(String projectIdStr, String srcIndexName, String dstIndexName) throws IOException {
        String readAliasName = inf.findReadAlias(projectIdStr);
        boolean ack_read = iac.prepareAliases().addAlias(dstIndexName, readAliasName).removeAlias(srcIndexName, readAliasName).get().isAcknowledged();
        log.info("alias {} created: {}", readAliasName, Boolean.toString(ack_read));

        String writeAliasName = inf.findWriteAlias(projectIdStr);
        boolean ack_write = iac.prepareAliases().addAlias(dstIndexName, writeAliasName).removeAlias(srcIndexName, writeAliasName).get().isAcknowledged();
        log.info("alias {} created: {}", readAliasName, Boolean.toString(ack_write));

        ien.changeEnabling(srcIndexName, false);
        ien.changeEnabling(dstIndexName, true);
    }
}
