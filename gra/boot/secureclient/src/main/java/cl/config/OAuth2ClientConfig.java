package cl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class OAuth2ClientConfig {
    @Bean
    @ConfigurationProperties("security.oauth2.localas.client")
    public ClientCredentialsResourceDetails localAuthServerResourceDetails() {
        log.info("enter localAuthServerResourceDetails");
        return new ClientCredentialsResourceDetails();
    }

    @Bean
    public OAuth2RestTemplate localAuthServerRestTemplate() {
        log.info("enter localAuthServerRestTemplate");
        return new OAuth2RestTemplate(localAuthServerResourceDetails());
    }
}
