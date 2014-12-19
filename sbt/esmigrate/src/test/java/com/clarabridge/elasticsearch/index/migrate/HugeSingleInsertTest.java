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

public class HugeSingleInsertTest extends ESTestBase {
    private static final Logger log = LoggerFactory.getLogger(HugeSingleInsertTest.class);

    private static final long MAX_ITER = 1000000;

    private final static long PROJECT_ID = 2;
    private final static String INDEX_NAME = Long.toString(PROJECT_ID);

    private final static long DOC_ID = 999999;
    private static final long SLEEP_BETWEEN_DOCS_MS = 0;


    private IndexResponse addDoc(String indexName, long docId) throws Exception {
        final String documentId = Long.toString(docId);

        IndexRequestBuilder reqb = client.prepareIndex(indexName, ESConstants.TYPE_DOCUMENT, documentId)
            .setSource(getSimpleDocContent(documentId, "new msg "))
            //.setRouting(routing)
            //.setRefresh(true)
            ;
        return reqb.get();
    }

    private void onIteration(long i) {
        if (SLEEP_BETWEEN_DOCS_MS >= 0) {
            try {
                Thread.sleep(SLEEP_BETWEEN_DOCS_MS);
            } catch (InterruptedException e) {
            }
        }
        if (i % 100 == 0) {
            log.info("iteration: {}", i);
        }
    }

    @Test
    public void testAddDocsSingle() throws Exception {
        for (long i = 0; i < MAX_ITER; ++i) {
            IndexResponse rsp = addDoc(INDEX_NAME, DOC_ID);
            assertNotNull(rsp);
            //assertTrue(rsp.isCreated());
            onIteration(i);
        }
        assertTrue(true);
    }
}
