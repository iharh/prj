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

public class RoutingTest extends ESTestBase {
    private static final Logger log = LoggerFactory.getLogger(RoutingTest.class);

    private final static long projectId = 1738;
    private final static String documentId = "1";

    @Ignore
    public void testDelDoc() throws Exception {
        String aliasName = "" + projectId; 
        String naturalId = "tag:search.twitter.com,2005:524293909925486593";
        String routing = naturalId.toLowerCase();

        DeleteRequestBuilder reqb = client.prepareDelete(aliasName, ESConstants.TYPE_DOCUMENT, documentId)
            .setRouting(routing)
            .setRefresh(true);

        DeleteResponse rsp = reqb.get();
        assertNotNull(rsp);
        assertTrue(rsp.isFound());
    }

    @Ignore
    public void testAddDoc() throws Exception {
        String aliasName = "write_" + projectId; 
        //String aliasName = "" + projectId; 
        String naturalId = "tag:search.twitter.com,2005:524293909925486593";
        String routing = naturalId.toLowerCase();

        XContentBuilder xcb = jsonBuilder().startObject()
            .field("naturalId", naturalId) //Boolean.toString(true)
            .endObject();

        //log.debug("xcb: {}", xcb.string());

        IndexRequestBuilder reqb = client.prepareIndex(aliasName, ESConstants.TYPE_DOCUMENT, documentId)
            .setSource(xcb)
            .setRouting(routing)
            .setRefresh(true);

        IndexResponse rsp = reqb.get();
        assertNotNull(rsp);
        assertTrue(rsp.isCreated());
    }
}
