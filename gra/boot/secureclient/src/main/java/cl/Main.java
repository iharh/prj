package cl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"cl"})
public class Main implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hello main!");
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
