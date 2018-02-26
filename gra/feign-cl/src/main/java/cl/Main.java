package cl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import lombok.extern.slf4j.Slf4j;

@EnableAutoConfiguration
@Slf4j
public class Main implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        log.debug("start");
    }
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
