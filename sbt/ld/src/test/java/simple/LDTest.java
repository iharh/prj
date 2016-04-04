package simple;

import org.junit.Test;
import org.junit.Ignore;

import com.clarabridge.transformer.ld.CrossModel;
import com.clarabridge.transformer.ld.NormLangDetector;
import com.clarabridge.transformer.ld.StringSourceIterator;
import com.clarabridge.transformer.ld.NormCrossModelScorer.Result;
import com.clarabridge.transformer.ld.compiler.Compiler;
import com.clarabridge.transformer.ld.utils.FileNamesCollector;

import java.io.File;

import static org.junit.Assert.assertTrue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LDTest {
    private static final Logger log = LoggerFactory.getLogger(LDTest.class);

    @Test
    public void testDetect() throws Exception {
        File modelDir = new File("/data/wrk/clb/ld");
        FileNamesCollector modelCollector = new FileNamesCollector();

        modelCollector.addFileMask(modelDir, "*.ldm");
        log.info("Loading language detector models from \"{}\"", modelDir.getCanonicalPath());
        log.info("Found {} model(s)", modelCollector.getFiles().size());

        CrossModel crossModel = Compiler.createModel(modelCollector.getFiles());
        final double desiredConfidenceLevel = 0.01; // 0.5;
        NormLangDetector langDetector = new NormLangDetector(crossModel, desiredConfidenceLevel);

        final String text = "This is my round table";
        final int maxWidthToTest = text.length();
        final StringSourceIterator sourceIterator =  new StringSourceIterator(text, maxWidthToTest);

        Result res = langDetector.analyse(sourceIterator);

        log.info("detected lang code: {}", res.getLangCode());
        log.info("confidence level: {}", res.getConfidenceLevel());

        assertTrue(true);
    }
}
