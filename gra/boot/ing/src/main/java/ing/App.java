package ing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {
    // for test only
    public String getGreeting() {
        return "Hello world.";
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
