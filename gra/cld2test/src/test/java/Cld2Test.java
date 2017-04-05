import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

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

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import org.apache.commons.lang3.SystemUtils;

import java.util.Map;
import java.util.List;
import java.util.Arrays;

import java.io.File;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.*;

@Slf4j
public class Cld2Test {
    private static final String CLD2_VER = "1.0.0";
    private static final boolean isLin = SystemUtils.IS_OS_LINUX;
    private static final String libName = String.format("cld2-%s-%s", (isLin ? "linux" : "windows"), CLD2_VER);
    private static final String inFileDir = (isLin ? "/data/wrk/clb" : "D:/clb/src") + "/spikes/iharh/ld/selected";

    @Encoding("UTF-8")
    public static interface LibCld2 {
        int detectLangClb(String text);
        int getMemoryUsage();
    }

    private void processForLang(LibCld2 cld2, CSVFormat csvFormat, String langCode) throws IOException {
        final String inFileName = inFileDir + "/" + langCode + ".csv";
        final CSVParser csvParser = CSVParser.parse(new File(inFileName), UTF_8, csvFormat);
        for (final CSVRecord r : csvParser) {
            final String text = r.get(0);
            int result = cld2.detectLangClb(text);
            //log.info("l: {}, code: {}, text: {}", text.length(), result, text);
        }
    }

    //FUTURE:
    //https://github.com/dropwizard/metrics/blob/master/metrics-core/src/main/java/io/dropwizard/metrics/MetricName.java
    //  metrics.register("threads", new ThreadStatesGaugeSet());
    //  metrics.register("system", new PublicMetricSetAdapter(new SystemPublicMetrics()));
    //public interface MetricSet extends Metric {
    //    Map<MetricName, Metric> getMetrics();
    //}

    public void doIter(LibCld2 cld2, CSVFormat csvFormat, List<String> langCodes, Map<String, Gauge> gauges) throws IOException {
        for (String langCode : langCodes) {
            processForLang(cld2, csvFormat, langCode);
        }
        Long valInit = (Long)gauges.get("memory.total.init").getValue();
        Long valMax  = (Long)gauges.get("memory.total.max").getValue();
        Long valUsed = (Long)gauges.get("memory.total.used").getValue();
        Long valCommitted = (Long)gauges.get("memory.total.committed").getValue();
        int nativeMemUsage = cld2.getMemoryUsage();
        log.info("{}, {}, {}, {}, {}", nativeMemUsage, valInit, valMax, valUsed, valCommitted);
    }

    private LibCld2 getCld2() {
        return LibraryLoader.create(LibCld2.class).load(libName);
    }

    @Test
    public void testCld() throws Exception {
        log.info("native, total.init, total.max, total.used, total.committed");

        final MetricRegistry metrics = new MetricRegistry();
        metrics.register("memory", new MemoryUsageGaugeSet());

        LibCld2 cld2 = getCld2();
        assertThat(cld2, is(notNullValue()));

        final CSVFormat csvFormat = CSVFormat.DEFAULT
            .withIgnoreSurroundingSpaces(true)
            .withIgnoreEmptyLines(false)
            .withSkipHeaderRecord(true)
            .withHeader("TEXT");

        List<String> langCodes = Arrays.asList("de", "ar", "en", "es", "fr", "it", "ja", "ko", "nl", "pt", "ru", "tr", "zh");
        //List<String> langCodes = Arrays.asList("en");
        final Map<String, Gauge> gauges = metrics.getGauges();

        Scheduler s = Schedulers.newParallel("cld2thread");

        Flux.range(1, 10)
            .parallel(2)
            .runOn(s)
            //.doOnEach((v) -> log.info("{} - {}", Thread.currentThread().getName(), v))
            .doOnNext((v) -> {
                try {
                    //log.info("start {} - {}", Thread.currentThread().getName(), v);
                    doIter(cld2, csvFormat, langCodes, gauges);
                    //log.info("finish {} - {}", Thread.currentThread().getName(), v);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            })
            .sequential()
            .blockLast()
            ;
    }

    @Ignore
    public void testMisc() throws Exception {
        LibCld2 cld2 = getCld2();
        assertThat(cld2, is(notNullValue()));
        //26 - de?
        assertThat(cld2.detectLangClb("I know and like so much my round table"), is(0));
        assertThat(cld2.detectLangClb("quatre petits enfants, trois filles malades"), is(4));
    }

    @Ignore
    public void testParallel() throws Exception {
        Scheduler s = Schedulers.newParallel("cld2thread");

        Flux.range(1, 10)
            .parallel(2)
            .runOn(s)
            //.doOnEach((v) -> log.info("{} - {}", Thread.currentThread().getName(), v))
            .doOnNext((v) -> log.info("{} - {}", Thread.currentThread().getName(), v))
            .sequential()
            .blockLast()
            ;
    }
}
