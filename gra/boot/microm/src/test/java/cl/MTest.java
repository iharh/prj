package cl;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootContextLoader;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@ContextConfiguration(loader = SpringBootContextLoader.class)
@SpringBootTest
public class MTest {
    private static final String M_NAME = "test";
    private static final TimeUnit measureUnit = TimeUnit.MILLISECONDS;

    @Configuration
    static class Config {
        @Bean
        MeterRegistry meterRegistry() {
            return new SimpleMeterRegistry();
        }
    }

    @Autowired
    private MeterRegistry meterRegistry;

    @Test
    public void mTest() throws Exception {
        assertThat(meterRegistry).isNotNull();
        final MStatsCollector collector = new MStatsCollector(meterRegistry);
        assertThat(collector).isNotNull();

        collector.start(M_NAME);
        measureUnit.sleep(100);
        collector.finish(M_NAME);

        final Timer t = collector.getTimerFor(M_NAME);
        assertThat(t).isNotNull();
        assertThat(t.count()).isEqualTo(1);
        assertThat(t.mean(measureUnit)).isLessThan(200.0);
    }
}
