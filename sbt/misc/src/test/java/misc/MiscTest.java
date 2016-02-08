package misc;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MiscTest {
    private static final Logger log = LoggerFactory.getLogger(MiscTest.class);

    @Test
    public void testMisc() throws Exception {
        final String clientId = "dev";
        final String secretKey = "12345";
        final String s = DatatypeConverter.printBase64Binary((clientId + ":" + secretKey).getBytes(StandardCharsets.UTF_8));
        log.info("misc: {}", s);
        assertTrue(true);
    }
}

