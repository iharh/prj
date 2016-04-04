package simple;

import org.junit.Test;
import org.junit.Ignore;

import com.clarabridge.transformer.ld.CrossModel;
import com.clarabridge.transformer.ld.NormLangDetector;
import com.clarabridge.transformer.ld.StringSourceIterator;
import com.clarabridge.transformer.ld.NormCrossModelScorer.Result;
import com.clarabridge.transformer.ld.compiler.Compiler;
import com.clarabridge.transformer.ld.utils.FileNamesCollector;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
//import java.io.Reader;
//import java.io.FileReader;

import static org.junit.Assert.assertTrue;
import static java.nio.charset.StandardCharsets.*;

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

        final CSVFormat csvFormat = CSVFormat.DEFAULT
            .withIgnoreSurroundingSpaces(true)
            .withHeader("TEXT");

        final String iFileName = "/data/wrk/prj/docker/cld/util/data/en.csv";
        //final Reader inReader = new FileReader(iFileName);
        final CSVParser csvParser = CSVParser.parse(new File(iFileName), UTF_8, csvFormat);
        for (final CSVRecord r : csvParser) {
            //log.info("r TEXT: {}", r.get("TEXT"));

            final String text = r.get(0);
            final int maxWidthToTest = text.length();
            final StringSourceIterator sourceIterator =  new StringSourceIterator(text, maxWidthToTest);

            Result res = langDetector.analyse(sourceIterator);

            log.info("lang code: {}, conf: {}", res.getLangCode(), Double.toString(res.getConfidenceLevel()));
        }
        assertTrue(true);
    }
}
