package cl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SecureApplication {
    public static void main(String[] args) {
        SpringApplication.run(SecureApplication.class, args);
    }
}
