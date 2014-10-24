package es;

import org.elasticsearch.client.IndicesAdminClient;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.hppc.cursors.ObjectObjectCursor;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class IndexGenerationFinder {
    private static final Logger log = LoggerFactory.getLogger(IndexGenerationFinder.class);

    private static final String INDEX_GENERATION_SEPARATOR = "_";
    private static final String WILDCARD = "*";


    private IndicesAdminClient iac;

    public IndexGenerationFinder(IndicesAdminClient iac) {
        this.iac = iac;
    }

    public String findCur(String projectIdStr) {
        return projectIdStr + INDEX_GENERATION_SEPARATOR + Long.toString(findGeneration(projectIdStr));
    }

    public String findNext(String projectIdStr) {
        return projectIdStr + INDEX_GENERATION_SEPARATOR + Long.toString(findGeneration(projectIdStr) + 1);
    }

    private long findGeneration(String projectIdStr) {
        int projectIdPrefixLen = projectIdStr.length() + 1;
        ImmutableOpenMap<String, Settings> is = iac.prepareGetSettings(projectIdStr + INDEX_GENERATION_SEPARATOR + WILDCARD).get().getIndexToSettings();

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
