package com.clarabridge.elasticsearch.index.migrate;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.elasticsearch.action.delete.DeleteRequestBuilder;
//import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleDeleteTest extends ESTestBase {
    private static final Logger log = LoggerFactory.getLogger(SingleDeleteTest.class);

    private final static long PROJECT_ID = 2;
    private final static String INDEX_NAME = Long.toString(PROJECT_ID);

    private final static long DOC_ID = 999999;

    @Test
    public void testAddDocsSingle() throws Exception {
        final String documentId = Long.toString(DOC_ID);

        DeleteRequestBuilder reqb = client.prepareDelete(INDEX_NAME, ESConstants.TYPE_DOCUMENT, documentId)
            //.setRouting(routing)
            //.setRefresh(true)
            ;
        DeleteResponse rsp = reqb.get();
        assertNotNull(rsp);
        log.info("delete resp found: {}, ver: {}", rsp.isFound(), rsp.getVersion());
    }
}
