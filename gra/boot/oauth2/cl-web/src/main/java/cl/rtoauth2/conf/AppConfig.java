package cl.rtoauth2.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.DefaultAccessTokenRequest;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
// import org.springframework.web.client.RestTemplate;

// import lombok.extern.slf4j.Slf4j;

@Configuration
// @Slf4j
public class AppConfig {
    @Bean
    public DefaultOAuth2ClientContext clbOAuth2ClientContext() {
        return new DefaultOAuth2ClientContext(new DefaultAccessTokenRequest());
    }

    @Bean
    @ConfigurationProperties("security.oauth2.clb.client")
    public OAuth2ProtectedResourceDetails clbAuthServerResourceDetails() {
        return new ClientCredentialsResourceDetails();
    }

    @Bean
    public OAuth2RestTemplate restTemplate() {
        final OAuth2ProtectedResourceDetails resource = clbAuthServerResourceDetails();
        final OAuth2ClientContext clientContext = clbOAuth2ClientContext();

        // log.info("enter restTemplate resource class: {}", resource.getClass().getName());
        ClientCredentialsAccessTokenProvider tokenProvider = new ClientCredentialsAccessTokenProvider();

        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(resource, clientContext);
        restTemplate.setAccessTokenProvider(tokenProvider);
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        return restTemplate;
    }
}