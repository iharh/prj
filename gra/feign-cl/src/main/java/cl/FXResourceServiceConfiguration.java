package cl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;

@Configuration
public class FXResourceServiceConfiguration {
    @Bean
    RequestInterceptor feignRequestInterceptor() {
        return new BasicAuthRequestInterceptor("admin", "admin");
    }
}
