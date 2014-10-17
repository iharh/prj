package es;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;

import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.node.Node;
import org.elasticsearch.client.Client;

import org.elasticsearch.action.admin.indices.alias.Alias;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EsTest {
    private static final Logger log = LoggerFactory.getLogger(EsTest.class);

    private void createIndex(Client client, long projectId) {
        String indexName = Long.toString(projectId);
        String aliasRead = "read_" + indexName;
        String aliasWrite = "write_" + indexName;

        if (client.admin().indices().prepareExists(indexName).execute().actionGet().isExists()) {
            assertTrue(client.admin().indices().prepareDelete(indexName).execute().actionGet().isAcknowledged());
        }

        assertTrue(client.admin().indices().prepareCreate(indexName)
            .setSettings(settingsBuilder().put("number_of_shards", Integer.toString(2)))
            .addAlias(new Alias(aliasRead)).addAlias(new Alias(aliasWrite))
            .get().isAcknowledged());
    }
            
    @Test
    public void testES() throws Exception {
        Settings settings = settingsBuilder()
            .put("discovery.zen.ping.unicast.hosts", "localhost")
            .put("discovery.zen.ping.multicast.enabled", false)
            .put("node.master", false)
            .build();

        // data(false).
        try (Node node = nodeBuilder().clusterName("elasticsearch").client(true).settings(settings).node()) {
            Client client = node.client();
            for (long i = 0; i < 3; ++i) {
                createIndex(client, i);
            } 

            // assertTrue(client.admin().indices().prepareAliases().addAlias("0", "read_2").execute().actionGet().isAcknowledged());
            // removeAlias
        }
    }
}
