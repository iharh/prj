package com.clarabridge.elasticsearch.index.migrate;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

//import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertAcked;

//import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.node.Node;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.hppc.cursors.ObjectObjectCursor;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.HashSet;

public class IndexMigrateTest extends ESTestBase {
    private static final Logger log = LoggerFactory.getLogger(IndexMigrateTest.class);

    private void deleteIndexIfExists(IndicesAdminClient iac, String indexName) {
        if (iac.prepareExists(indexName).get().isExists()) {
            boolean ack = iac.prepareDelete(indexName).get().isAcknowledged();
            log.info("index {} deleted: {}", indexName, Boolean.toString(ack));
        }
    }

    private void createIndex(IndicesAdminClient iac, long projectId, long generation) {
        String projectIdStr = Long.toString(projectId);
        String generationStr = Long.toString(generation);
        String indexName = projectIdStr + "_" + generationStr;
        //String aliasRead = "read_" + projectIdStr;
        //String aliasWrite = "write_" + projectIdStr;

        deleteIndexIfExists(iac, indexName);

        boolean ack = iac.prepareCreate(indexName)
            .setSettings(settingsBuilder().put("number_of_shards", Integer.toString(2)))
            //.addAlias(new Alias(aliasRead)).addAlias(new Alias(aliasWrite))
            .get().isAcknowledged();

        log.info("index {} created: {}", indexName, Boolean.toString(ack));
    }

    private final static long projectId = 2514;
    //
    //final long projectId = 3;

    @Ignore
    public void testCreateAliasesIfNeeded() throws Exception {
        IndexNameFinder inf = new IndexNameFinder(iac);
        inf.createAliasesIfNeeded(Long.toString(projectId));
        assertTrue(true);
    }

    @Ignore
    public void testMigrateIndex() throws Exception {
        //IndicesAdminClient iac = client.admin().indices();
        //for (long i = 0; i < 3; ++i) {
        //    createIndex(iac, projectId, i);
        //} 

        Set<String> dvFields = new HashSet<String>();

        /*
        dvFields.add("_verbatim");
        dvFields.add("_mstokenname");
        dvFields.add("_words");
        dvFields.add("_languageDetected");

        dvFields.add("_languagedetected");

        dvFields.add("natural_id");
        dvFields.add("_id_source");

        dvFields.add("_id_sentence");
        dvFields.add("_verbatimtype");

        dvFields.add("_tokendata");

        dvFields.add("_doc_time");
        dvFields.add("_doc_date");
        */

        IndexMigrator im = new IndexMigrator(client);

        IndexMigrateRequest req = new IndexMigrateRequest();
        req.setProjectId(projectId);
        req.setShards(5);
        req.setBatchSize(1000);
        req.setWriteThreads(4);
        req.setDvFields(dvFields);
        //req.setObsolete(true);

        im.migrateIndex(req);
    }

    @Ignore
    public void testSwitchIndex() throws Exception {
        IndexMigrator im = new IndexMigrator(client);
        im.switchIndex(projectId, 1);
    }
}
