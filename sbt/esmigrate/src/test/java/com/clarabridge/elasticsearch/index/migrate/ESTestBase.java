package com.clarabridge.elasticsearch.index.migrate;

import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertTrue;
//import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertAcked;

import org.elasticsearch.node.Node;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;

import org.elasticsearch.cluster.metadata.IndexMetaData;

import org.elasticsearch.index.shard.service.InternalIndexShard;


import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ESTestBase {
    private static final Logger log = LoggerFactory.getLogger(ESTestBase.class);

    // look for auto-filling stuff:
    // http://www.elasticsearch.org/guide/en/elasticsearch/client/java-api/current/index_.html
    
    private Node node;
    protected Client client;
    protected IndicesAdminClient iac;

    private final static String clusterName = "epbygomw0024-5432-postgres-win_ss";
    //
    //final String clusterName = "elasticsearch";

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
        iac = client.admin().indices();
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

    protected void deleteIndexIfExists(String indexName) {
        if (iac.prepareExists(indexName).get().isExists()) {
            boolean ack = iac.prepareDelete(indexName).get().isAcknowledged();
            log.info("index {} deleted: {}", indexName, Boolean.toString(ack));
        }
    }

    protected void createSimpleIndex(String indexName) {
        boolean ack = iac.prepareCreate(indexName)
            .setSettings(
                settingsBuilder()
                    .put(IndexMetaData.SETTING_CREATION_DATE, 5l)
                    .put(InternalIndexShard.INDEX_REFRESH_INTERVAL, "600s") // -1 for disabling
            )
            .addMapping(ESConstants.TYPE_DOCUMENT, // jsonBuilder();
                "natural_id", "type=string,store=true",
                "msg"       , "type=string,store=true"
            ).get().isAcknowledged();

        log.info("index {} created: {}", indexName, Boolean.toString(ack));
    }

    protected void reCreateSimpleIndex(String indexName) throws Exception {
        deleteIndexIfExists(indexName);
        createSimpleIndex(indexName);
    }

    protected XContentBuilder getSimpleDocContent(String documentId, String msgPrefix) throws IOException {
        XContentBuilder result = jsonBuilder().startObject()
            .field("natural_id", documentId)
            .field("msg", msgPrefix + documentId)
            .endObject();
        //log.debug("xcb: {}", result.string());
        return result;
    }
}
