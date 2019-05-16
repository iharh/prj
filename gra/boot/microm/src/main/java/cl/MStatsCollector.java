package cl;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MStatsCollector {
    private static final String keyPrefix = "test.bench.";

    private MeterRegistry meterRegistry;
    private TimeUnit measureUnit;

    private Map<String, Timer.Sample> timerSamples = new HashMap<>();
    private Map<String, Timer> timers = new LinkedHashMap<>();

    public MStatsCollector(MeterRegistry meterRegistry, TimeUnit measureUnit) {
        this.meterRegistry = meterRegistry;
        this.measureUnit = measureUnit;
    }

    public void start(String name) {
        log.debug("start for: {}", name);
        timerSamples.put(name, Timer.start(meterRegistry));
    }

    public void finish(String name) {
        log.debug("finish for: {}", name);
        Timer.Sample sample = timerSamples.get(name);
        Timer timer = meterRegistry.timer(keyPrefix + name);
        sample.stop(timer);
        timers.putIfAbsent(name, timer);
    }

    public Timer getTimerFor(String name) {
        return timers.get(name);
    }
}
