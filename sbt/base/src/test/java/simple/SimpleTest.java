package simple;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleTest {
    private static final Logger log = LoggerFactory.getLogger(SimpleTest.class);

    @Test
    public void testSimple() throws Exception {
        log.info("simple");
        assertTrue(true);
    }
}

