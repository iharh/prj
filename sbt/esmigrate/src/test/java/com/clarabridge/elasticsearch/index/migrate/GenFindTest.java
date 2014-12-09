package com.clarabridge.elasticsearch.index.migrate;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.search.SearchResponse;


import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenFindTest extends ESTestBase {
    private static final Logger log = LoggerFactory.getLogger(GenFindTest.class);

    private final static long projectId = 1404;

    @Test
    public void testFindGen() throws Exception {
        assertTrue(true);
    }
}
