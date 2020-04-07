package simple;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class SimpleApp implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("run called");
    }
 
    public static void main(String[] args) {
        SpringApplication.run(SimpleApp.class, args);
    }
}
