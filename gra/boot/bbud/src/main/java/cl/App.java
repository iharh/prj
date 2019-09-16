package cl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("app start");
        Simple s = new Simple();
        s.tell();
        log.info("app finish");
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
