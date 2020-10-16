package metrics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.bytedeco.javacpp.Pointer;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MockClock;
import io.micrometer.core.instrument.binder.BaseUnits;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusDurationNamingConvention;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import io.prometheus.client.CollectorRegistry;

import java.util.concurrent.atomic.AtomicInteger;

import static io.micrometer.core.instrument.MockClock.clock;
import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MetricsTests {
    private AtomicInteger n = new AtomicInteger(7);
    // private CollectorRegistry prometheusRegistry = new CollectorRegistry(true);
    // private MockClock clock = new MockClock();
    private PrometheusMeterRegistry registry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT); // , prometheusRegistry, clock);

    @BeforeEach
    void before() {
        registry.config().namingConvention(new PrometheusDurationNamingConvention());
    }

    @Test
    public void testGauge() throws Exception {
        Gauge.builder("gauge", this, MetricsTests::getN)
            //.tags("a", "b")
            // .baseUnit(BaseUnits.BYTES)
            .register(registry);

        String scraped = registry.scrape();
        log.info("scraped 1: {}", scraped);

        assertThat(scraped)
            //.contains("gauge_bytes")
            .contains("gauge")
        ;
    }

    public int getN() {
        return n.get();
    }

    public long javacppPhysicalBytes() {
        return Pointer.physicalBytes();
    }
}
