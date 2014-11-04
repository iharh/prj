package com.clarabridge.elasticsearch.ingex.migrate;

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

public class IndexMigrateTest {
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

    // look for auto-filling stuff:
    // http://www.elasticsearch.org/guide/en/elasticsearch/client/java-api/current/index_.html
    
    private Node node;
    private Client client;

    private final static String clusterName = "epbygomw0024-5432-postgres-win_ss";
    private final static long projectId = 1404;
    //
    //final String clusterName = "elasticsearch";
    //final long projectId = 3;

    @Before
    public void setUp() {
        Settings settings = settingsBuilder()
            //.put("http.port", "9200")
            .put("discovery.zen.ping.unicast.hosts", "localhost")
            .put("discovery.zen.ping.multicast.enabled", false)
            .put("node.master", false)
            .build();

        node = nodeBuilder()
            .clusterName(clusterName)
            .client(true)
            .settings(settings)
            .node(); // data(false).

        client = node.client();
    }

    @After
    public void tearDown() {
        try {
            if (node != null) {
                node.close();
            }
        } catch (Throwable t) {
        }
    }

    @Ignore
    public void testMigrateIndex() throws Exception {
        //IndicesAdminClient iac = client.admin().indices();
        //for (long i = 0; i < 3; ++i) {
        //    createIndex(iac, projectId, i);
        //} 

        IndexMigrator im = new IndexMigrator(client);
        im.migrateIndex(projectId, 5, 1000, 4, false);
        //im.checkIndexAliases(projectId);
    }

    @Test
    public void testSwitchIndex() throws Exception {
        IndexMigrator im = new IndexMigrator(client);
        im.switchIndex(projectId, 1);
    }
}
