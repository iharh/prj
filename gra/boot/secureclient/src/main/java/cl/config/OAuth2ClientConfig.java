package cl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;

import feign.RequestInterceptor;

import org.apache.commons.lang3.exception.ExceptionUtils;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class OAuth2ClientConfig {
    @Bean
    @ConfigurationProperties("security.oauth2.localas.client")
    public ClientCredentialsResourceDetails localAuthServerResourceDetails() {
        return new ClientCredentialsResourceDetails();
    }

    /*@Bean
    public OAuth2RestTemplate localAuthServerRestTemplate() {
        ClientCredentialsResourceDetails rd = localAuthServerResourceDetails();
        log.info("rd1: {}", rd.getAccessTokenUri());
        return new OAuth2RestTemplate(localAuthServerResourceDetails());
    }*/

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        ClientCredentialsResourceDetails rd = localAuthServerResourceDetails();
        log.info("rd2: {}", rd.getAccessTokenUri());
        /*try {
            throw new IllegalArgumentException();
        } catch (Exception e) {
            log.info("rd2 st: {}", ExceptionUtils.getStackTrace(e));
        }*/
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), rd);
    }
}
