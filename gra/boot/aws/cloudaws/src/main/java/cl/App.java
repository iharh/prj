package cl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class App implements CommandLineRunner {
    @Value("${cloud.aws.bucket}")
    private String bucket;
    
    public String getGreeting() {
        return "Hello world.";
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("greeting: {} bucket: {}", getGreeting(), bucket);
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
