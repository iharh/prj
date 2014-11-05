package com.clarabridge.elasticsearch.ingex.migrate;

import org.elasticsearch.client.IndicesAdminClient;

import org.elasticsearch.cluster.metadata.MappingMetaData;

import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.hppc.cursors.ObjectObjectCursor;

import org.elasticsearch.action.admin.indices.mapping.get.GetMappingsResponse;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import java.util.Map;

public class IndexEnabler {
    private static final Logger log = LoggerFactory.getLogger(IndexEnabler.class);

    public static final String TYPE_CLB_META = "clb_meta"; //$NON-NLS-1$
    public static final String OBJ_META = "_meta"; //$NON-NLS-1$
    public static final String FIELD_ENABLED = "enabled"; //$NON-NLS-1$

    private IndicesAdminClient iac;

    public IndexEnabler(IndicesAdminClient iac) {
        this.iac = iac;
    }

    public void changeEnabling(String indexName, boolean val) throws IOException {
        boolean ack_dst = iac.preparePutMapping().setIndices(indexName).setType(TYPE_CLB_META).setSource(
            jsonBuilder().startObject().startObject(TYPE_CLB_META).startObject(OBJ_META)
            .field(FIELD_ENABLED, Boolean.toString(val))
            .endObject().endObject().endObject()
        ).get().isAcknowledged();
        log.info("index: {} metadata(index-enabled) changed: {}", indexName, Boolean.toString(ack_dst));
    }

    public boolean isEnabled(String indexName) throws IOException {
        GetMappingsResponse resp = iac.prepareGetMappings(indexName).setTypes(TYPE_CLB_META).get();
        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> indicesToMappings = resp.getMappings();
        ImmutableOpenMap<String, MappingMetaData> indexMappings = indicesToMappings.get(indexName);
        return indexMappings == null ? true : isEnabledMMD(indexMappings.get(TYPE_CLB_META));
    }

    public boolean isEnabledMMD(MappingMetaData mmd) throws IOException {
        if (mmd == null) {
            return true;
        }
        Map<String, Object> typeMappingsMap = mmd.sourceAsMap();
        Object metaObj = typeMappingsMap.get(OBJ_META);
        if (metaObj == null) {
            return true;
        }
        @SuppressWarnings("unchecked")
        Map<String, Object> meta = (Map<String, Object>)metaObj;
        Object enabledObj = meta.get(FIELD_ENABLED);
        if (enabledObj == null) {
            return true;
        }
        return "true".equalsIgnoreCase(enabledObj.toString()); //$NON-NLS-1$
    }
}
