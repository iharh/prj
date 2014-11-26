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
//import org.elasticsearch.common.collect.ImmutableOpenMap;
//import org.elasticsearch.common.hppc.cursors.ObjectObjectCursor;

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

    @Test
    public void testAddDoc() throws Exception {
        String s1 = jsonBuilder().startObject().startObject("clb_meta").startObject("obj_meta")
            .field("enabled", Boolean.toString(true))
            .endObject().endObject().endObject()
            .string();

        log.debug("s1: {}", s1);

        assertTrue(true);

        /*
        IndexRequestBuilder insertDocument = clientWrapper.getClient()
            .prepareIndex(clientWrapper.aliasForWrite(), TYPE_DOCUMENT, documentId)
            .setSource(createDocument(document));

        insertDocument.setRouting(routing);

        documentIds.add(document.getDocumentId());

	private XContentBuilder createDocument(DocumentVO doc) throws IOException {
		XContentBuilder b = jsonBuilder();
		b.startObject();
		doc.populateDocument(b);
		b.endObject();
		return b;
	}
	
	private XContentBuilder createVerbatim(DocumentVO doc) throws IOException {
		XContentBuilder b = jsonBuilder();
		b.startObject();
		doc.populateVerbatim(b);
		b.endObject();
		return b;
	}	
    
	private XContentBuilder createSentence(DocumentVO doc, Set<Float> sentiments, Set<Integer> words) throws IOException {
		XContentBuilder b = jsonBuilder();
		b.startObject();
		doc.populateSentense(b);
		b.endObject();
		return b;
	}		

        b.field(field.name, field.value);
        */
    }
}
