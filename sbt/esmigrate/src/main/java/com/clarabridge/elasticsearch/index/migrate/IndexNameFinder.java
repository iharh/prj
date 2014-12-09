package com.clarabridge.elasticsearch.index.migrate;

import org.elasticsearch.client.IndicesAdminClient;

//import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.hppc.cursors.ObjectCursor;
//import org.elasticsearch.common.hppc.cursors.ObjectObjectCursor;

//import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.cluster.metadata.AliasMetaData;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import java.io.IOException;

import java.util.List;

public class IndexNameFinder {
    private static final Logger log = LoggerFactory.getLogger(IndexNameFinder.class);

    private static final String SEPARATOR = "_"; //$NON-NLS-1$ 
    private static final String WILDCARD = "*"; //$NON-NLS-1$
    private static final String READ_ALIAS_PREFIX = "read"; //$NON-NLS-1$
    private static final String WRITE_ALIAS_PREFIX = "write"; //$NON-NLS-1$


    private IndicesAdminClient iac;
    //private IndexEnabler ien;

    public IndexNameFinder(IndicesAdminClient iac) {
        this.iac = iac;
        //ien = new IndexEnabler(iac);
    }

    public void createAliasesIfNeeded(String projectIdStr) {
        String indexName = findCur(projectIdStr);
        if (iac.prepareExists(indexName).execute().actionGet().isExists()) {
            addAliasIfNeeded(indexName, findReadAlias(projectIdStr));
            addAliasIfNeeded(indexName, findWriteAlias(projectIdStr));
        }
    }

    private void addAliasIfNeeded(String indexName, String aliasName) {
        if (!iac.prepareAliasesExist(aliasName).get().exists()) {
            boolean ack = iac.prepareAliases().addAlias(indexName, aliasName).get().isAcknowledged();
            log.info("alias: {} created: {}", aliasName, Boolean.toString(ack));
        }
    }

    public String findReadAlias(String projectIdStr) {
        return READ_ALIAS_PREFIX + SEPARATOR + projectIdStr;
    }

    public String findWriteAlias(String projectIdStr) {
        return WRITE_ALIAS_PREFIX + SEPARATOR + projectIdStr;
    }

    public String findCur(String projectIdStr) {
        long gen = findGeneration(projectIdStr);
        return gen < 0 ? projectIdStr :
            projectIdStr + SEPARATOR + Long.toString(gen);
    }

    public String findNext(String projectIdStr) {
        long gen = findGeneration(projectIdStr);
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

    private long findGeneration(String projectIdStr) {
        return findGenerationByAlias(projectIdStr);
    }

    private long findGenerationByAlias(String projectIdStr) {
        String aliasName = findReadAlias(projectIdStr);
        int projectIdPrefixLen = projectIdStr.length() + 1;

        // TODO: maybe throw the exception if the alias does not exists?
        long result = -1;

        ImmutableOpenMap<String, List<AliasMetaData>> aliasesByIndices = iac.prepareGetAliases(aliasName).get().getAliases();
        for (ObjectCursor<String> c : aliasesByIndices.keys()) {
            final String indexName = c.value;
            log.info("alias: {} traversing index: {}", aliasName, indexName);
            try {
                result = Math.max(result, Long.parseLong(indexName.substring(projectIdPrefixLen)));
            } catch (StringIndexOutOfBoundsException | NumberFormatException e) {
            }
        }

        if (result < 0 && !iac.prepareExists(projectIdStr).get().isExists()) {
            result = 0; // new index - start from generation 0
        }

        log.info("found generation: {} for project: {}", result, projectIdStr);

        return result;
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
