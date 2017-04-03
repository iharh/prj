import junit.framework.TestCase;

import lombok.extern.slf4j.Slf4j;

import jnr.ffi.LibraryLoader;
import jnr.ffi.annotations.Encoding;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.codahale.metrics.Gauge;
//import com.codahale.metrics.Metric;
//import com.codahale.metrics.MetricFilter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.MetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;

import org.apache.commons.lang3.SystemUtils;

import java.util.Map;
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
        final String inFileDir = (SystemUtils.IS_OS_LINUX ? "/data/wrk/clb" : "D:/clb/src") + "/spikes/iharh/ld/selected";
        final String inFileName = inFileDir + "/" + langCode + ".csv";

        final CSVParser csvParser = CSVParser.parse(new File(inFileName), UTF_8, csvFormat);
        for (final CSVRecord r : csvParser) {
            final String text = r.get(0);
            //Normalizer.normalize(text, Normalizer.Form.NFD); // NFC - does not work
            int result = cld2.detectLangClb(text);
            //log.info("l: {}, code: {}, text: {}", text.length(), result, text);
        }
    }

    //  metricRegistry.register("memory", new MemoryUsageGaugeSet());
    //  metricRegistry.register("threads", new ThreadStatesGaugeSet());
    //  metricRegistry.register("system", new PublicMetricSetAdapter(new SystemPublicMetrics()));

    //public interface MetricSet extends Metric {
    //    Map<MetricName, Metric> getMetrics();
    //}

    //FUTURE:
    //https://github.com/dropwizard/metrics/blob/master/metrics-core/src/main/java/io/dropwizard/metrics/MetricName.java

    /*private void registerAll(String prefix, MetricSet metricSet, MetricRegistry registry) {
        for (Map.Entry<String, Metric> entry : metricSet.getMetrics().entrySet()) {
            if (entry.getValue() instanceof MetricSet) {
                registerAll(prefix + "." + entry.getKey(), (MetricSet) entry.getValue(), registry);
            } else {
                registry.register(prefix + "." + entry.getKey(), entry.getValue());
            }
        }
    }*/

    public void doIter(LibCld2 cld2, CSVFormat csvFormat, MetricRegistry registry) throws Exception {
        //List<String> langCodes = Arrays.asList("de", "ar", "en", "es", "fr", "it", "ja", "ko", "nl", "pt", "ru", "tr", "zh");
        List<String> langCodes = Arrays.asList("en");
        for (String langCode : langCodes) {
            processForLang(cld2, csvFormat, langCode);
        }

        //assertEquals(0, cld2.detectLangClb("I know and like so much my round table"));
        //assertEquals(4, cld2.detectLangClb("quatre petits enfants, trois filles malades"));
        //26 - de
        //

        Map<String, Gauge> vals = registry.getGauges();

        Object valInit = vals.get("memory.total.init").getValue();
        Object valMax = vals.get("memory.total.max").getValue();
        Object valUsed = vals.get("memory.total.used").getValue();
        log.info("init: {}, max: {}, used: {}", valInit, valMax, valUsed);
    }

    public void testMy() throws Exception {
        log.info("start");

        final MetricRegistry registry = new MetricRegistry();
        registry.register("memory", new MemoryUsageGaugeSet());

        final String libName = String.format("cld2-%s-%s", (SystemUtils.IS_OS_LINUX ? "linux" : "windows"), CLD2_VER);
        LibCld2 cld2 = LibraryLoader.create(LibCld2.class).load(libName);
        assertNotNull(cld2);

        final CSVFormat csvFormat = CSVFormat.DEFAULT
            .withIgnoreSurroundingSpaces(true)
            .withIgnoreEmptyLines(false)
            .withSkipHeaderRecord(true)
            .withHeader("TEXT");

        for (int i = 0; i < 2; ++i) {
            doIter(cld2, csvFormat, registry);
        }

        log.info("finish");
    }
}
