package cl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import feign.RequestInterceptor;
import feign.auth.BasicAuthRequestInterceptor;

import lombok.Getter;

@Configuration
public class FXResourceServiceConfiguration {
    @Getter
    @Value("${cmp.usr}")
    private String cmpUsr;

    @Getter
    @Value("${cmp.pwd}")
    private String cmpPwd;

    @Bean
    RequestInterceptor feignRequestInterceptor() {
        return new BasicAuthRequestInterceptor(cmpUsr, cmpPwd);
    }
}
