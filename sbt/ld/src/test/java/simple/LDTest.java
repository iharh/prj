package simple;

import org.junit.Test;
import org.junit.Ignore;

import com.clarabridge.transformer.ld.CrossModel;
import com.clarabridge.transformer.ld.NormLangDetector;
import com.clarabridge.transformer.ld.StringSourceIterator;
import com.clarabridge.transformer.ld.NormCrossModelScorer.Result;
import com.clarabridge.transformer.ld.compiler.Compiler;
import com.clarabridge.transformer.ld.utils.FileNamesCollector;
import com.clarabridge.transformer.ld.exceptions.ModelCreatorException;
import com.clarabridge.transformer.ld.exceptions.MathException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
//import java.io.Reader;
//import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertTrue;
import static java.nio.charset.StandardCharsets.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LDTest {
    private static final Logger log = LoggerFactory.getLogger(LDTest.class);

    private static NormLangDetector getLangDetector(String modelDirName) throws IOException, ModelCreatorException {
        FileNamesCollector modelCollector = new FileNamesCollector();

        File modelDir = new File(modelDirName);
        log.info("Loading language detector models from \"{}\"", modelDir.getCanonicalPath());

        modelCollector.addFileMask(modelDir, "*.ldm");
        log.info("Found {} model(s)", modelCollector.getFiles().size());

        CrossModel crossModel = Compiler.createModel(modelCollector.getFiles());
        final double desiredConfidenceLevel = 0.01; // 0.5;
        return new NormLangDetector(crossModel, desiredConfidenceLevel);
    }

    private static void process(final NormLangDetector langDetector, String inFileName) throws IOException, ModelCreatorException, MathException {
        final CSVFormat csvFormat = CSVFormat.DEFAULT
            .withIgnoreSurroundingSpaces(true)
            .withHeader("TEXT");

        final CSVParser csvParser = CSVParser.parse(new File(inFileName), UTF_8, csvFormat);
        for (final CSVRecord r : csvParser) {
            final String text = r.get(0);

            final int maxWidthToTest = text.length();
            final StringSourceIterator sourceIterator =  new StringSourceIterator(text, maxWidthToTest);

            Result res = langDetector.analyse(sourceIterator); // throws ModelCreatorException, MathException

            log.info("lang code: {}, conf: {}", res.getLangCode(), Double.toString(res.getConfidenceLevel()));
        }
    }

    @Test
    public void testDetect() throws Exception {
        final NormLangDetector langDetector = getLangDetector("/data/wrk/clb/ld");
        final String inFileName = "/data/wrk/prj/docker/cld/util/data/en.csv";
        process(langDetector, inFileName);
        assertTrue(true);
    }
}
