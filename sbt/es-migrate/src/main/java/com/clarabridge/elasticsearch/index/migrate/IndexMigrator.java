package com.clarabridge.elasticsearch.index.migrate;

//import com.clarabridge.transformer.indexing.pipe.ElasticSearchIndexer;

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

import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.index.IndexRequest;

import org.elasticsearch.index.fielddata.FieldDataType;
import org.elasticsearch.index.mapper.core.StringFieldMapper;
import org.elasticsearch.index.mapper.internal.RoutingFieldMapper;

import org.elasticsearch.search.SearchHit;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import org.apache.commons.lang.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

public class IndexMigrator {
    private static final Logger log = LoggerFactory.getLogger(IndexMigrator.class);

    public static final String INDEX_SETTING_NUMBER_OF_SHARDS = "number_of_shards"; //$NON-NLS-1$

    public static final String TYPE_PERCOLATOR = "percolator_mapping"; //$NON-NLS-1$
    public static final String MAPPING_PROPERTIES = "properties"; //$NON-NLS-1$

    public static final String FIELDDATA = "fielddata"; //$NON-NLS-1$

    private static final String ERR_INVALID_GEN = "Invalid generation: %s"; //$NON-NLS-1$


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

    public void switchIndex(long projectId, long generation) throws IOException {
        String projectIdStr = Long.toString(projectId);
        String srcIndexName = inf.findCur(projectIdStr);
        String dstIndexName = inf.findSpecific(projectIdStr, generation);
        if (dstIndexName == null) {
            if (generation > 0) {
                throw new IllegalArgumentException(String.format(ERR_INVALID_GEN, generation));
            }
            dstIndexName = projectIdStr; // old-style index name
        }
        log.info("switching index: {} to index: {}", srcIndexName, dstIndexName);

        if (!srcIndexName.equals(dstIndexName)) {
            switchAliasesAndEnabling(projectIdStr, srcIndexName, dstIndexName);
        }
    }

    public void migrateIndex(IndexMigrateRequest req) throws IOException {
        long projectId = req.getProjectId();
        int shards = req.getShards();
        int batchSize = req.getBatchSize();
        int writeThreads = req.getWriteThreads();
        Set<String> dvFields = req.getDvFields();
        boolean obsolete = req.getObsolete();
        TimeValue sleepBetweenBatches = req.getSleepBetweenBatches();

        String projectIdStr = Long.toString(projectId);
        String srcIndexName = inf.findCur(projectIdStr);
        String dstIndexName = obsolete ? inf.findObsolete(projectIdStr) : inf.findNext(projectIdStr);
        log.info("migrating index: {} to index: {}", srcIndexName, dstIndexName);

        Set<String> typesWithParent = new HashSet<String>();

        createIndexCopyMetadata(typesWithParent, srcIndexName, dstIndexName, shards, dvFields);

        waitForClusterAndRefresh(srcIndexName);

        ClbFieldChecker fieldChecker = null; // new ClbFieldChecker();

        try (IndexLocker il = new IndexLocker(iac, srcIndexName);
            BulkWaiter bw = new BulkWaiter();
            BulkProcessor bp = BulkProcessor.builder(client, bw)
                .setBulkActions(batchSize) 
                .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB)) 
                .setConcurrentRequests(writeThreads) 
                //.setConcurrentRequests(0)
                .build();
        ) {
            allDocsToBulk(bp, bw, typesWithParent, fieldChecker, srcIndexName, dstIndexName, shards, batchSize, sleepBetweenBatches);
        } catch (Exception e) {
            throw new IOException(e.getMessage(), e);
        }

        waitForClusterAndRefresh(dstIndexName);

        switchAliasesAndEnabling(projectIdStr, srcIndexName, dstIndexName);
    }


    private void createIndexCopyMetadata(Set<String> typesWithParent, String srcIndexName, String dstIndexName, int shards, Set<String> dvFields) throws IOException {
        deleteIndexIfExists(dstIndexName);

        CreateIndexRequestBuilder rb = iac.prepareCreate(dstIndexName);
        rb = addIndexMappings(rb, typesWithParent, srcIndexName, dvFields);
        rb = addIndexSettings(rb                 , srcIndexName, shards);
        boolean ack = rb.get().isAcknowledged();
        log.info("index {} created: {}", dstIndexName, Boolean.toString(ack));
    }

    private void allDocsToBulk(BulkProcessor bp, BulkWaiter bw,
        Set<String> typesWithParent, ClbFieldChecker fieldChecker, String srcIndexName, String dstIndexName, int shards, int batchSize, TimeValue sleepBetweenBatches) throws Exception {

        SearchRequestBuilder srb = client.prepareSearch(srcIndexName)
            //.setTypes(type)
            .setSearchType(SearchType.SCAN)
            .setScroll(TimeValue.timeValueHours(240)) // timeValueMinutes(2)
            .setQuery(matchAllQuery())
            .setSize(batchSize/shards)
        ;
        for (int i = 0; i < ClbRoutingFinder.usedESFieldNames.length; ++i) {
            srb.addFieldDataField(ClbRoutingFinder.usedESFieldNames[i]);
        }
        for (int i = 0; i < ClbParentFinder.usedESFieldNames.length; ++i) {
            srb.addFieldDataField(ClbParentFinder.usedESFieldNames[i]);
        }

        SearchResponse resp = srb.get();
        do {
            bw.checkErrors();

            if (sleepBetweenBatches != null) {
                try {
                    long sleepMillis = sleepBetweenBatches.getMillis();
                    log.info("sleeping between batches: {}", sleepMillis);
                    Thread.sleep(sleepMillis);
                } catch (InterruptedException e) {
                }
            }

            for (SearchHit hit : resp.getHits()) {
                String hitType = hit.getType();
                int hitShard = hit.getShard().getShardId();

                log.debug("hit type: {}, id: {}, shard: {}", hitType, hit.getId(), hitShard); // ", srcRef: {},  hit.getSourceRef().toUtf8()

                IndexRequest ir = new IndexRequest(dstIndexName)
                    .source(hit.getSourceRef(), true)
                    .type(hitType)
                    .id(hit.getId());

                if (typesWithParent.contains(hitType)) {
                    String parentVal = ClbParentFinder.getParentValue(hit);
                    if (!StringUtils.isBlank(parentVal)) {
                        String parentES = hit.field(ClbParentFinder.PARENT).<String>value();
                        log.debug("hit parent: {}, es: {}", parentVal, parentES);
                        //if (!parentVal.equals(parentES)) {
                        //    log.info("!!! hit parent: {}, es: {}", parentVal, parentES);
                        //}
                        ir.parent(parentVal);
                    }
                }

                String routingVal = ClbRoutingFinder.getRoutingValue(hit);
                if (routingVal != null) {
                    ir.routing(routingVal);
                }

                bp.add(ir);

                if (fieldChecker != null) {
                    fieldChecker.addHit(hit);
                }
            }
            resp = client.prepareSearchScroll(resp.getScrollId())
                .setScroll(TimeValue.timeValueHours(240)) // timeValueMinutes(10)
                .get();
        }
        while (resp.getHits().hits().length > 0);
    }

    private CreateIndexRequestBuilder addIndexMappings(CreateIndexRequestBuilder rb, Set<String> typesWithParent, String indexName, Set<String> dvFields) throws IOException {
        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> indicesToMappings =
            iac.prepareGetMappings(indexName).get().mappings();
        ImmutableOpenMap<String, MappingMetaData> indexMappings = indicesToMappings.get(indexName);

        Map<String, Object> enableDocValue = new HashMap<String, Object>(1);
        enableDocValue.put(FieldDataType.FORMAT_KEY, FieldDataType.DOC_VALUES_FORMAT_VALUE);

        for (ObjectObjectCursor<String, MappingMetaData> c : indexMappings) {
            String mappingType = c.key;
            log.debug("mapping for type: {}", mappingType);
            Map<String, Object> typeMappingsMap = c.value.sourceAsMap();

            // for (String k : typeMappingsMap.keySet()) { log.debug("mapping for: {}", k); }
            if (typeMappingsMap.containsKey(ClbParentFinder.PARENT)) {
                typesWithParent.add(mappingType);
            }

            if (!TYPE_PERCOLATOR.equals(mappingType)) {
                @SuppressWarnings("unchecked")
                Map<String, Object> propertiesMap = (Map<String, Object>)typeMappingsMap.get(MAPPING_PROPERTIES);
                //for (String k : propertiesMap.keySet()) { log.debug("mapping properties for: {}", k); }

                for(Map.Entry<String, Object> entry : propertiesMap.entrySet()) {
                    String fieldName = entry.getKey();

                    @SuppressWarnings("unchecked")
                    Map<String, Object> fieldValueMap = (Map<String, Object>)entry.getValue(); // LinkedHashMap

                    if (dvFields != null && dvFields.contains(fieldName)) {
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
                    } else { // clear DocValue if present
                        fieldValueMap.remove(FieldDataType.DOC_VALUES_FORMAT_VALUE);

                        @SuppressWarnings("unchecked")
                        Map<String, Object> fieldDataMap = (Map<String, Object>)fieldValueMap.get(FIELDDATA);

                        if (fieldDataMap != null) {
                            @SuppressWarnings("unchecked")
                            String formatVal = (String)fieldDataMap.get(FieldDataType.FORMAT_KEY);

                            if (FieldDataType.DOC_VALUES_FORMAT_VALUE.equalsIgnoreCase(formatVal)) {
                                fieldDataMap.remove(FieldDataType.FORMAT_KEY);
                            }
                            if (fieldDataMap.isEmpty()) {
                                fieldValueMap.remove(FIELDDATA);
                            }
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
        IndicesAliasesRequestBuilder iarb_read = iac.prepareAliases().addAlias(dstIndexName, readAliasName);
        if (iac.prepareExists(srcIndexName).get().isExists()) {
            iarb_read = iarb_read.removeAlias(srcIndexName, readAliasName);
        }
        boolean ack_read = iarb_read.get().isAcknowledged();
        log.info("alias {} created: {}", readAliasName, Boolean.toString(ack_read));

        String writeAliasName = inf.findWriteAlias(projectIdStr);
        IndicesAliasesRequestBuilder iarb_write = iac.prepareAliases().addAlias(dstIndexName, writeAliasName);
        if (iac.prepareExists(srcIndexName).get().isExists()) {
            iarb_write = iarb_write.removeAlias(srcIndexName, writeAliasName);
        }
        boolean ack_write = iarb_write.get().isAcknowledged();
        log.info("alias {} created: {}", writeAliasName, Boolean.toString(ack_write));

        //ien.changeEnabling(srcIndexName, false);
        //ien.changeEnabling(dstIndexName, true);
    }
}
