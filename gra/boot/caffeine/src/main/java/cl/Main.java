package cl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        log.info("start");
    }
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
