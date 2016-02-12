package es;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ESTest {
    private static final Logger log = LoggerFactory.getLogger(ESTest.class);

    @Test
    public void testSimple() throws Exception {
        log.info("es");
        assertTrue(true);
    }
}

