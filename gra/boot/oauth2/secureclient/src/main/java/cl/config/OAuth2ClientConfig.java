package cl.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import feign.RequestInterceptor;

import java.util.List;

@Configuration
public class OAuth2ClientConfig {
    @Bean
    @ConfigurationProperties("security.oauth2.localas.client")
    public ClientCredentialsResourceDetails localAuthServerResourceDetails() {
        ClientCredentialsResourceDetails result = new ClientCredentialsResourceDetails();
        result.setScope(List.of("cx-designer.api[group='lexicons-nlpsvc-export']"));
        return result;
    }

    @Bean
    public RequestInterceptor oauth2FeignRequestInterceptor() {
        return new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), localAuthServerResourceDetails());
    }

    @Bean
    public OAuth2RestTemplate localAuthServerRestTemplate() {
        return new OAuth2RestTemplate(localAuthServerResourceDetails());
    }
}
