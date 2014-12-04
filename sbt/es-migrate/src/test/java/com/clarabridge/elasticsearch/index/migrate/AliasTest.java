package com.clarabridge.elasticsearch.index.migrate;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertEquals;

//import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertAcked;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AliasTest extends ESTestBase {
    private static final Logger log = LoggerFactory.getLogger(AliasTest.class);

    private final static long projectId = 1404;

    @Ignore
    public void testAlias() throws Exception {
        final String readAliasName = "read_" + projectId;
        assertTrue(iac.prepareAliasesExist(readAliasName).get().exists());
    }
}
