package com.clarabridge.elasticsearch.ingex.migrate;

import org.elasticsearch.client.IndicesAdminClient;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.hppc.cursors.ObjectObjectCursor;

import org.elasticsearch.cluster.metadata.MappingMetaData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class IndexNameFinder {
    private static final Logger log = LoggerFactory.getLogger(IndexNameFinder.class);

    private static final String SEPARATOR = "_"; //$NON-NLS-1$ 
    private static final String WILDCARD = "*"; //$NON-NLS-1$
    private static final String READ_ALIAS_PREFIX = "read"; //$NON-NLS-1$
    private static final String WRITE_ALIAS_PREFIX = "write"; //$NON-NLS-1$


    private IndicesAdminClient iac;
    private IndexEnabler ien;

    public IndexNameFinder(IndicesAdminClient iac) {
        this.iac = iac;
        ien = new IndexEnabler(iac);
    }

    public String findReadAlias(String projectIdStr) {
        return READ_ALIAS_PREFIX + SEPARATOR + projectIdStr;
    }

    public String findWriteAlias(String projectIdStr) {
        return WRITE_ALIAS_PREFIX + SEPARATOR + projectIdStr;
    }

    public String findCur(String projectIdStr) {
        long gen = findGenerationByMeta(projectIdStr);
        return gen < 0 ? projectIdStr :
            projectIdStr + SEPARATOR + Long.toString(gen);
    }

    public String findNext(String projectIdStr) {
        long gen = findGenerationByMeta(projectIdStr);
        gen = gen < 0 ? 1 : gen + 1;
        return projectIdStr + SEPARATOR + Long.toString(gen);
    }

    public String findObsolete(String projectIdStr) {
        return projectIdStr;
    }

    public String findSpecific(String projectIdStr, long gen) {
        String indexName = projectIdStr + SEPARATOR + gen;
        return iac.prepareExists(indexName).get().isExists() ? indexName : null;
    }

    private long findGenerationByMeta(String projectIdStr) {
        int projectIdPrefixLen = projectIdStr.length() + 1;
        //log.info("find generation for project: {}", projectIdStr);
        String indexMask = projectIdStr + SEPARATOR + WILDCARD;

        //.setTypes(TYPE_CLB_META)
        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> itm = iac.prepareGetMappings(indexMask).get().getMappings();

        long result = 0;
        boolean found = false;
        for (ObjectObjectCursor<String, ImmutableOpenMap<String, MappingMetaData>> c : itm) {
            String indexName = c.key;
            ImmutableOpenMap<String, MappingMetaData> indexMappings = itm.get(indexName);
            try {
                boolean enabled = indexMappings == null ? true : ien.isEnabledMMD(indexMappings.get(IndexEnabler.TYPE_CLB_META));
                //log.info("traversing index: {} enabled: {}", indexName, enabled);
                if (enabled) {
                    long generation = Long.parseLong(indexName.substring(projectIdPrefixLen));
                    if (generation > result)
                        result = generation;
                    found = true;
                }
            } catch (IOException | NumberFormatException e) {
            }
        }

        if (!found && iac.prepareExists(projectIdStr).get().isExists()) {
            //log.info("found old-style index name (without generation)");
            result = -1;
        }

        //log.info("found generation: {} for project: {}", result, projectIdStr);

        return result;
    }
/*
    private long findGenerationBySettings(String projectIdStr) {
        int projectIdPrefixLen = projectIdStr.length() + 1;
        //log.info("find generation for project: {}", projectIdStr);
        String indexMask = projectIdStr + SEPARATOR + WILDCARD;
        ImmutableOpenMap<String, Settings> is = iac.prepareGetSettings(indexMask).get().getIndexToSettings();

        long result = 0;
        boolean found = false;
        for (ObjectObjectCursor<String, Settings> c : is) {
            //log.info("traversing index: {}", c.key);
            try {
                long generation = Long.parseLong(c.key.substring(projectIdPrefixLen));
                if (generation > result)
                    result = generation;
                found = true;
            } catch (NumberFormatException e) {
            }
        }

        if (!found && iac.prepareExists(projectIdStr).get().isExists()) {
            //log.info("found old-format index");
            result = -1; // old-style index name (without generation)
        }

        return result;
    }
*/
}
