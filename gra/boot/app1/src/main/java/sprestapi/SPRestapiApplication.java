package sprestapi;

import sprestapi.configs.RestAppConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;

@Import(value = RestAppConfig.class)
public class SPRestapiApplication {
    public static void main(String[] args) {
        new SpringApplication(RestAppConfig.class).run(args);
    }
}
