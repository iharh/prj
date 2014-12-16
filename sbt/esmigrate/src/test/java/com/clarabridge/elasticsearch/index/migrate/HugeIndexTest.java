package com.clarabridge.elasticsearch.index.migrate;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;


import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HugeIndexTest extends ESTestBase {
    private static final Logger log = LoggerFactory.getLogger(RoutingTest.class);

    private final static long projectId = 2;
    private final static String indexName = Long.toString(projectId);

    private IndexResponse addDoc(long docId) throws Exception {
        final String documentId = Long.toString(docId);

        XContentBuilder xcb = jsonBuilder().startObject()
            .field("natural_id", Long.toString(docId))
            .field("msg", "msg " + docId)
            .endObject();

        //log.debug("xcb: {}", xcb.string());

        IndexRequestBuilder reqb = client.prepareIndex(indexName, ESConstants.TYPE_DOCUMENT, documentId)
            .setSource(xcb)
            //.setRouting(routing)
            .setRefresh(true);

        return reqb.get();
    }

    @Test
    public void testAddDoc() throws Exception {
        for (long i = 1; i <= 1000000; ++i) {
            IndexResponse rsp = addDoc(i);
            assertNotNull(rsp);
            //assertTrue(rsp.isCreated());
        }
    }
}
