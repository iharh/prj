package ld;

import org.junit.Test;
import org.junit.Ignore;

import com.clarabridge.transformer.ld.NormLangDetector;

import static org.junit.Assert.assertTrue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LDTest {
    private static final Logger log = LoggerFactory.getLogger(LDTest.class);

    @Test
    public void testDetect() throws Exception {
        final NormLangDetector langDetector = LD.getLangDetector("/data/wrk/clb/ld");
        final String inFileDir = "/data/wrk/clb/spikes/iharh/ld/selected";
        //final String inFileDir = "D:/clb/src/spikes/iharh/ld/selected";
        final String expectedCode = "en";
        LD.process(langDetector, inFileDir, expectedCode);
        assertTrue(true);
    }
}
