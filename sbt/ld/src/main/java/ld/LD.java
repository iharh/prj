package ld;

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
import java.io.IOException;

import static java.nio.charset.StandardCharsets.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LD {
    private static final Logger log = LoggerFactory.getLogger(LD.class);
    
    private static final double DESIRED_CONFIDENCE_LEVEL = 0.01; // 0.5;

    public static NormLangDetector getLangDetector(String modelDirName) throws IOException, ModelCreatorException {
        FileNamesCollector modelCollector = new FileNamesCollector();

        File modelDir = new File(modelDirName);
        log.debug("Loading language detector models from \"{}\"", modelDir.getCanonicalPath());

        modelCollector.addFileMask(modelDir, "*.ldm");
        log.debug("Found {} model(s)", modelCollector.getFiles().size());

        CrossModel crossModel = Compiler.createModel(modelCollector.getFiles());
        return new NormLangDetector(crossModel, DESIRED_CONFIDENCE_LEVEL);
    }

    public static void process(final NormLangDetector langDetector, String inFileDir, String expectedCode) throws IOException, ModelCreatorException, MathException {
        System.out.println("#CLB LD");
        System.out.println("#LINE, EXPECTED, DETECTED, TEXT");

        final CSVFormat csvFormat = CSVFormat.DEFAULT
            .withIgnoreSurroundingSpaces(true)
            .withHeader("TEXT");

        final String inFileName = inFileDir + "/" + expectedCode + ".csv";
        final CSVParser csvParser = CSVParser.parse(new File(inFileName), UTF_8, csvFormat);
        int rows = 0;
        int rowsMismatch = 0;
        for (final CSVRecord r : csvParser) {
            final String text = r.get(0);

            final int maxWidthToTest = text.length();
            final StringSourceIterator sourceIterator =  new StringSourceIterator(text, maxWidthToTest);

            Result res = langDetector.analyse(sourceIterator); // throws ModelCreatorException, MathException

            final String detectedCode = res.getConfidenceLevel() > DESIRED_CONFIDENCE_LEVEL ? res.getLangCode() : "un";

            if (!expectedCode.equals(detectedCode)) {
                ++rowsMismatch;
                System.out.println(Integer.toString(rows + 2) + "," + expectedCode + "," + detectedCode + "," + text);
            }
            ++rows;
        }
        System.out.println("");
        System.out.println("rows: " + rows + ", rowsMismatch: " + rowsMismatch);
    }

    public static void main(String [] args) {
        try {
            final String modelDirName = args[1]; // "/data/wrk/clb/ld"
            final NormLangDetector langDetector = LD.getLangDetector(modelDirName);

            final String inFileDir = "/data/wrk/clb/spikes/iharh/ld/selected";
            final String expectedCode = "en";
            LD.process(langDetector, inFileDir, expectedCode);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace(System.err);
            System.exit(1);
        }
    }
}
