package es;

import org.junit.Test;
import org.junit.Ignore;

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

public class EsTest {
    private static final Logger log = LoggerFactory.getLogger(EsTest.class);

    private void deleteIndexIfExists(IndicesAdminClient iac, String indexName) {
        if (iac.prepareExists(indexName).execute().actionGet().isExists()) {
            boolean ack = iac.prepareDelete(indexName).execute().actionGet().isAcknowledged();
            log.info("index {} deleted: {}", indexName, Boolean.toString(ack));
        }
    }

    private void createIndex(IndicesAdminClient iac, long projectId, long ver) {
        String projectIdStr = Long.toString(projectId);
        String verStr = Long.toString(ver);
        String indexName = projectIdStr + "_" + verStr;
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

    @Test
    public void testMigrateIndex() throws Exception {
        //final String clusterName = "epbygomw0024-5432-postgres-win_ss";
        final String clusterName = "elasticsearch";
        //final long projectId = 1404;
        final long projectId = 3;

        Settings settings = settingsBuilder()
            //.put("http.port", "9200")
            .put("discovery.zen.ping.unicast.hosts", "localhost")
            .put("discovery.zen.ping.multicast.enabled", false)
            .put("node.master", false)
            .build();

        Node node = null;
        try {
            node = nodeBuilder()
                .clusterName(clusterName)
                .client(true)
                .settings(settings)
                .node(); // data(false).
            Client client = node.client();

            IndicesAdminClient iac = client.admin().indices();
            //for (long i = 0; i < 3; ++i) {
            //    createIndex(iac, projectId, i);
            //} 

            //IndexMigrator im = new IndexMigrator(client);
            //im.migrateIndex(projectId, 5, 1000, 4);

            ImmutableOpenMap<String, Settings> is = iac.prepareGetSettings("3_*").get().getIndexToSettings();

            for (ObjectObjectCursor<String, Settings> c : is) {
                log.info("found index: {}", c.key);
            }
        } finally {
            try {
                if (node != null) {
                    node.close();
                }
            } catch (Throwable t) {
            }
        }
    }
}
