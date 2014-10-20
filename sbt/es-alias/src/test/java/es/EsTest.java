package es;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.node.Node;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;

import org.elasticsearch.common.hppc.cursors.ObjectObjectCursor;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;

import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import java.util.Map;

public class EsTest {
    private static final Logger log = LoggerFactory.getLogger(EsTest.class);

    private void deleteIndexIfExists(Client client, String indexName) {
        if (client.admin().indices().prepareExists(indexName).execute().actionGet().isExists()) {
            assertTrue(client.admin().indices().prepareDelete(indexName).execute().actionGet().isAcknowledged());
        }
    }

    private void createIndex(Client client, long projectId) {
        String indexName = Long.toString(projectId);
        String aliasRead = "read_" + indexName;
        String aliasWrite = "write_" + indexName;

        deleteIndexIfExists(client, indexName);

        assertTrue(client.admin().indices().prepareCreate(indexName)
            .setSettings(settingsBuilder().put("number_of_shards", Integer.toString(2)))
            .addAlias(new Alias(aliasRead)).addAlias(new Alias(aliasWrite))
            .get().isAcknowledged());
    }

    private CreateIndexRequestBuilder addIndexMappings(Client client, String indexName, CreateIndexRequestBuilder rb) throws IOException {
        ImmutableOpenMap<String, ImmutableOpenMap<String, MappingMetaData>> indicesToMappings =
            client.admin().indices().prepareGetMappings(indexName).get().mappings();
        ImmutableOpenMap<String, MappingMetaData> indexMappings = indicesToMappings.get(indexName);

        for (ObjectObjectCursor<String, MappingMetaData> c : indexMappings) {
            //log.info("mapping for type: {}", c.key);
            Map<String, Object> typeMappingsMap = c.value.sourceAsMap();
            //for (String k : typeMappingsMap.keySet()) { log.info("mapping for: {}", k); }
            rb = rb.addMapping(c.key, typeMappingsMap);
        }
        return rb;
    }

    private CreateIndexRequestBuilder addIndexSettings(Client client, String indexName, CreateIndexRequestBuilder rb) {
        ImmutableOpenMap<String, Settings> indexToSettings = client.admin().indices().prepareGetSettings(indexName).get().getIndexToSettings();
        Settings indexSettings = indexToSettings.get(indexName);

        Map<String, String> indexSettingsMap = indexSettings.getAsMap(); // getAsStructuredMap();
        //for (String k : indexSettingsMap.keySet()) { log.info("settings for: {}", k); }
        return rb.setSettings(
            // "index.uuid" is auto-generated as a new one
            settingsBuilder().put(indexSettings).put("number_of_shards", "3")
        );
    }

    private void migrateIndex(Client client, long projectId) throws IOException {
        String srcIndexName = Long.toString(projectId);
        String dstIndexName = srcIndexName + "_0";

        deleteIndexIfExists(client, dstIndexName);

        CreateIndexRequestBuilder rb = client.admin().indices().prepareCreate(dstIndexName);
        rb = addIndexMappings(client, srcIndexName, rb);
        rb = addIndexSettings(client, srcIndexName, rb);

        // alias stuff
        // assertTrue(client.admin().indices().prepareAliases().addAlias("0", "read_2").execute().actionGet().isAcknowledged());
        // removeAlias

        assertTrue(rb.get().isAcknowledged());
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

            //for (long i = 0; i < 2; ++i) {
            //    createIndex(client, i);
            //} 

            // locking stuff
            //assertTrue(client.admin().indices().prepareUpdateSettings("1")
            //    .setSettings(settingsBuilder().put(IndexMetaData.SETTING_READ_ONLY, true))
            //    .get().isAcknowledged());

            migrateIndex(client, 1);        
        }
    }
}
