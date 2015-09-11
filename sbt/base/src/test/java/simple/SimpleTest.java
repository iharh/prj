package simple;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Formatter;

import java.io.IOException;

public class SimpleTest {
    private static final Logger log = LoggerFactory.getLogger(SimpleTest.class);

    @Test
    public void testSimple() throws Exception {
        assertTrue(true);
    }
}

