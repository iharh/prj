package cl;

import org.springframework.cloud.netflix.feign.EnableFeignClients;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;

import lombok.extern.slf4j.Slf4j;

@EnableAutoConfiguration
@ComponentScan("cl")
@EnableFeignClients
@Slf4j
public class Main implements CommandLineRunner {
    @Autowired
    private FXResourceService client;

    @Override
    public void run(String... args) throws Exception {
        log.info("start");
        ResponseEntity<String> response = client.exportLexicon(0, "Custom");
        log.info("Got feign response: {}", response);
    }
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
