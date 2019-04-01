package simple;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleApp {
    public static void main(String[] args) {
        new SpringApplication(SimpleApp.class).run(args);
    }
}

