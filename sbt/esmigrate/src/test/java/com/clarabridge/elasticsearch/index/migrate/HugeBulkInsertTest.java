package com.clarabridge.elasticsearch.index.migrate;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HugeBulkInsertTest extends ESTestBase {
    private static final Logger log = LoggerFactory.getLogger(HugeBulkInsertTest.class);

    private static final long MAX_DOCS = 1000000;

    private final static long projectId = 2;
    private final static String indexName = Long.toString(projectId);

    @Test
    public void testBulkInsert() throws Exception {
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
                    .source(getSimpleDocContent(id, "msg "))
                    .type(ESConstants.TYPE_DOCUMENT)
                    .id(id);

                bp.add(ir);
            }

            bw.checkErrors();
        }
    }
}
