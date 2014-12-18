package com.clarabridge.elasticsearch.index.migrate;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;

import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HugeIndexTest extends ESTestBase {
    private static final Logger log = LoggerFactory.getLogger(RoutingTest.class);

    private static final long MAX_DOCS = 1000000;

    private final static long projectId = 2;
    private final static String indexName = Long.toString(projectId);

    private XContentBuilder getDocContent(String documentId) throws IOException {
        XContentBuilder result = jsonBuilder().startObject()
            .field("natural_id", documentId)
            .field("msg", "msg " + documentId)
            .endObject();
        //log.debug("xcb: {}", result.string());
        return result;
    }

    private IndexResponse addDoc(long docId) throws Exception {
        final String documentId = Long.toString(docId);

        IndexRequestBuilder reqb = client.prepareIndex(indexName, ESConstants.TYPE_DOCUMENT, documentId)
            .setSource(getDocContent(documentId))
            //.setRouting(routing)
            //.setRefresh(true)
            ;
        return reqb.get();
    }

    @Ignore
    public void testAddDocsSingle() throws Exception {
        reCreateSimpleIndex(indexName);

        for (long i = 0; i < MAX_DOCS; ++i) {
            IndexResponse rsp = addDoc(i);
            assertNotNull(rsp);
            //assertTrue(rsp.isCreated());
        }
    }

    @Test
    public void testAddDocsBulk() throws Exception {
        reCreateSimpleIndex(indexName);

        try (BulkWaiter bw = new BulkWaiter();
            BulkProcessor bp = BulkProcessor.builder(client, bw)
                .setBulkActions(10000) 
                .setBulkSize(new ByteSizeValue(1, ByteSizeUnit.GB)) 
                .setConcurrentRequests(4) 
                .build();
        ) {
            for (long i = 0; i < MAX_DOCS; ++i) {
                String id = Long.toString(i);
                IndexRequest ir = new IndexRequest(indexName)
                    .source(getDocContent(id))
                    .type(ESConstants.TYPE_DOCUMENT)
                    .id(id);

                bp.add(ir);
            }

            bw.checkErrors();
        }
    }
}
