package simple;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LDTest {
    private static final Logger log = LoggerFactory.getLogger(LDTest.class);

    @Test
    public void testDetect() throws Exception {
        log.info("detect");
        assertTrue(true);
    }
}

