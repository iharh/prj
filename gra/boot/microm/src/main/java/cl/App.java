package cl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        log.info("hello world");
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
