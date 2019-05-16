package cl;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import io.micrometer.core.instrument.MeterRegistry;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class MTest {
    @Autowired
    private MeterRegistry meterRegistry;

    @Test
    public void mTest() throws Exception {
        final MStatsCollector collector = new MStatsCollector(meterRegistry);
        assertThat(collector).isNotNull();// isEqualTo(true);
    }
}
