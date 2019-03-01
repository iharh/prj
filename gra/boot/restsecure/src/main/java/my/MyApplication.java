package my;

//import my.config.MyAppConfig;
//import my.config.MySecurityConfig;

import org.springframework.boot.SpringApplication;

//import org.springframework.context.annotation.Import;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//@Import(value = MyAppConfig.class)
//@Import(value = MySecurityConfig.class)
public class MyApplication {
    public static void main(String[] args) {
        new SpringApplication(MyApplication.class).run(args); // MyAppConfig.class
    }
}
