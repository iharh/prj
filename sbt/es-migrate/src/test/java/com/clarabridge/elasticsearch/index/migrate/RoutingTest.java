package com.clarabridge.elasticsearch.index.migrate;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import com.clarabridge.transformer.indexing.pipe.ElasticSearchIndexer;

//import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertAcked;

//import org.elasticsearch.node.NodeBuilder;
import org.elasticsearch.node.Node;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
//import org.elasticsearch.common.collect.ImmutableOpenMap;
//import org.elasticsearch.common.hppc.cursors.ObjectObjectCursor;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;

import static org.elasticsearch.common.settings.ImmutableSettings.settingsBuilder;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import java.util.Set;
//import java.util.HashSet;

public class RoutingTest {
    private static final Logger log = LoggerFactory.getLogger(RoutingTest.class);

    // look for auto-filling stuff:
    // http://www.elasticsearch.org/guide/en/elasticsearch/client/java-api/current/index_.html
    
    private Node node;
    private Client client;

    private final static String clusterName = "epbygomw0024-5432-postgres-win_ss";
    private final static long projectId = 1738;
    private final static String documentId = "1";
    //private final static long projectId = 1404;
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
    public void testDelDoc() throws Exception {
        String aliasName = "" + projectId; 
        String naturalId = "tag:search.twitter.com,2005:524293909925486593";
        String routing = naturalId.toLowerCase();

        DeleteRequestBuilder reqb = client.prepareDelete(aliasName, ElasticSearchIndexer.TYPE_DOCUMENT, documentId)
            .setRouting(routing)
            .setRefresh(true);

        DeleteResponse rsp = reqb.get();
        assertNotNull(rsp);
        assertTrue(rsp.isFound());
    }

    @Test
    public void testAddDoc() throws Exception {
        String aliasName = "write_" + projectId; 
        //String aliasName = "" + projectId; 
        String naturalId = "tag:search.twitter.com,2005:524293909925486593";
        String routing = naturalId.toLowerCase();

        XContentBuilder xcb = jsonBuilder().startObject()
            .field("naturalId", naturalId) //Boolean.toString(true)
            .endObject();

        //log.debug("xcb: {}", xcb.string());

        IndexRequestBuilder reqb = client.prepareIndex(aliasName, ElasticSearchIndexer.TYPE_DOCUMENT, documentId)
            .setSource(xcb)
            .setRouting(routing)
            .setRefresh(true);

        IndexResponse rsp = reqb.get();
        assertNotNull(rsp);
        assertTrue(rsp.isCreated());
    }
}
