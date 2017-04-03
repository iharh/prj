import junit.framework.TestCase;

import lombok.extern.slf4j.Slf4j;

import jnr.ffi.LibraryLoader;
import jnr.ffi.annotations.Encoding;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.apache.commons.lang3.SystemUtils;

import java.util.List;
import java.util.Arrays;

import java.text.Normalizer;

import java.io.File;

import static java.nio.charset.StandardCharsets.*;

@Slf4j
public class MyTest extends TestCase {
    private static final String CLD2_VER = "1.0.0";

    @Encoding("UTF-8")
    public static interface LibCld2 {
        int detectLangClb(String text);
    }

    private void processForLang(LibCld2 cld2, CSVFormat csvFormat, String langCode) throws Exception {
        //final String inFileDir = "/data/wrk/clb/spikes/iharh/ld/selected";
        final String inFileDir = "D:/clb/src/spikes/iharh/ld/selected";

        final String inFileName = inFileDir + "/" + langCode + ".csv";

        final CSVParser csvParser = CSVParser.parse(new File(inFileName), UTF_8, csvFormat);
        for (final CSVRecord r : csvParser) {
            final String text = r.get(0);
            //Normalizer.normalize(text, Normalizer.Form.NFD); // NFC - does not work
            int result = cld2.detectLangClb(text);
            //log.info("l: {}, code: {}, text: {}", text.length(), result, text);
        }
    }

    public void testMy() throws Exception {
        log.info("start");

        final String libName = String.format("cld2-%s-%s", (SystemUtils.IS_OS_LINUX ? "linux" : "windows"), CLD2_VER);
        LibCld2 cld2 = LibraryLoader.create(LibCld2.class).load(libName);
        assertNotNull(cld2);

        final CSVFormat csvFormat = CSVFormat.DEFAULT
            .withIgnoreSurroundingSpaces(true)
            .withIgnoreEmptyLines(false)
            .withSkipHeaderRecord(true)
            .withHeader("TEXT");

        List<String> langCodes = Arrays.asList("de", "ar", "en", "es", "fr", "it", "ja", "ko", "nl", "pt", "ru", "tr", "zh");
        for (String langCode : langCodes) {
            processForLang(cld2, csvFormat, langCode);
        }

        //assertEquals(0, cld2.detectLangClb("I know and like so much my round table"));
        //assertEquals(4, cld2.detectLangClb("quatre petits enfants, trois filles malades"));
        //26 - de
        //

        log.info("finish");
    }
}
