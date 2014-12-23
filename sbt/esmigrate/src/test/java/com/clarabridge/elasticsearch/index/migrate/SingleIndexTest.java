package com.clarabridge.elasticsearch.index.migrate;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.elasticsearch.action.index.IndexRequestBuilder;
//import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleIndexTest extends ESTestBase {
    private static final Logger log = LoggerFactory.getLogger(SingleIndexTest.class);

    private final static long PROJECT_ID = 2;
    private final static String INDEX_NAME = Long.toString(PROJECT_ID);

    private final static long DOC_ID = 999999;

    @Test
    public void testAddDocsSingle() throws Exception {
        final String documentId = Long.toString(DOC_ID);

        IndexRequestBuilder reqb = client.prepareIndex(INDEX_NAME, ESConstants.TYPE_DOCUMENT, documentId)
            .setSource(getSimpleDocContent(documentId, "new msg "))
            //.setRouting(routing)
            //.setRefresh(true)
            ;
        IndexResponse rsp = reqb.get();
        assertNotNull(rsp);
        log.info("index resp created: {}, ver: {}", rsp.isCreated(), rsp.getVersion());
    }
}
