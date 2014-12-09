package com.clarabridge.elasticsearch.index.migrate;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;

import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.hppc.cursors.ObjectObjectCursor;

import org.elasticsearch.cluster.metadata.MappingMetaData;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenFindTest extends ESTestBase {
    private static final Logger log = LoggerFactory.getLogger(GenFindTest.class);

    private static final String SEPARATOR = "_"; //$NON-NLS-1$ 
    private static final String WILDCARD = "*"; //$NON-NLS-1$

    private final static long projectId = 1404;

    @Ignore
    public void testClosedIndexExists() throws Exception {
        String projectIdStr = Long.toString(projectId);
        String indexName = projectIdStr + SEPARATOR + "0";

        IndexNameFinder inf = new IndexNameFinder(iac);
        assertEquals(indexName, inf.findSpecific(projectIdStr, 0));
    }

    @Ignore
    public void testFindGen() throws Exception {
        String projectIdStr = Long.toString(projectId);
        String indexMask = projectIdStr + SEPARATOR + WILDCARD;

        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> itm = iac.prepareGetMappings(indexMask).get().getMappings();

        for (ObjectObjectCursor<String, ImmutableOpenMap<String, MappingMetaData>> c : itm) {
            String indexName = c.key;
            log.debug("found index gen: {}", indexName);
        }
    }

/*
    private long findGenerationByMeta(String projectIdStr) {
        int projectIdPrefixLen = projectIdStr.length() + 1;
        //log.info("find generation for project: {}", projectIdStr);
        String indexMask = projectIdStr + SEPARATOR + WILDCARD;

        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> itm = iac.prepareGetMappings(indexMask).get().getMappings();

        long result = 0;
        for (ObjectObjectCursor<String, ImmutableOpenMap<String, MappingMetaData>> c : itm) {
            String indexName = c.key;
            ImmutableOpenMap<String, MappingMetaData> indexMappings = itm.get(indexName);
            try {
                boolean enabled = indexMappings == null ? true : ien.isEnabledMMD(indexMappings.get(IndexEnabler.TYPE_CLB_META));
                //log.info("traversing index: {} enabled: {}", indexName, enabled);
                if (enabled) {
                    result = Math.max(result, Long.parseLong(indexName.substring(projectIdPrefixLen)));
                }
            } catch (IOException | NumberFormatException e) {
            }
        }

        if (result < 0 && !iac.prepareExists(projectIdStr).get().isExists()) {
            result = 0; // new index - start from generation 0
        }

        //log.info("found generation: {} for project: {}", result, projectIdStr);

        return result;
    }
*/

}
