package com;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.is;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestSimple {
    private static final Logger log = LoggerFactory.getLogger(TestSimple.class);

    @Test
    public void testSimple() throws Exception {
        log.debug("testSimple");

//        final String e = "abc";
//        assertThat(e, is("abc"));
        assertTrue(true);
    }
}
