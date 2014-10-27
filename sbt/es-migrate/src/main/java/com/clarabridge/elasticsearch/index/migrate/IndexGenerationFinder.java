package com.clarabridge.elasticsearch.ingex.migrate;

import org.elasticsearch.client.IndicesAdminClient;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.hppc.cursors.ObjectObjectCursor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class IndexGenerationFinder {
    private static final Logger log = LoggerFactory.getLogger(IndexGenerationFinder.class);

    private static final String SEPARATOR = "_"; //$NON-NLS-1$ 
    private static final String WILDCARD = "*"; //$NON-NLS-1$
    private static final String READ_ALIAS_PREFIX = "read"; //$NON-NLS-1$
    private static final String WRITE_ALIAS_PREFIX = "write"; //$NON-NLS-1$


    private IndicesAdminClient iac;

    public IndexGenerationFinder(IndicesAdminClient iac) {
        this.iac = iac;
    }

    public String findReadAlias(String projectIdStr) {
        return READ_ALIAS_PREFIX + SEPARATOR + projectIdStr;
    }

    public String findWriteAlias(String projectIdStr) {
        return WRITE_ALIAS_PREFIX + SEPARATOR + projectIdStr;
    }

    public String findCur(String projectIdStr) {
        return projectIdStr + SEPARATOR + Long.toString(findGeneration(projectIdStr));
    }

    public String findNext(String projectIdStr) {
        return projectIdStr + SEPARATOR + Long.toString(findGeneration(projectIdStr) + 1);
    }

    private long findGeneration(String projectIdStr) {
        int projectIdPrefixLen = projectIdStr.length() + 1;
        ImmutableOpenMap<String, Settings> is = iac.prepareGetSettings(projectIdStr + SEPARATOR + WILDCARD).get().getIndexToSettings();

        long result = 0;
        for (ObjectObjectCursor<String, Settings> c : is) {
            //log.debug("traversing index: {}", c.key);
            try {
                long generation = Long.parseLong(c.key.substring(projectIdPrefixLen));
                if (generation > result)
                    result = generation;
            } catch (NumberFormatException e) {
            }
        }
        return result;
    }
}
