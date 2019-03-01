package my;

import my.configs.MyAppConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Import;

@Import(value = MyAppConfig.class)
public class MyApplication {
    public static void main(String[] args) {
        new SpringApplication(MyAppConfig.class).run(args);
    }
}
