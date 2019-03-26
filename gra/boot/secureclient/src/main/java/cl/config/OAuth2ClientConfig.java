package cl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

//import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

//import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

//@EnableOAuth2Client
@Configuration
public class OAuth2ClientConfig {
    //@Autowired
    //OAuth2ClientContext oauth2ClientContext;

    @Bean
    @ConfigurationProperties("security.oauth2.localas.client")
    public ClientCredentialsResourceDetails localAuthServer() {
        return new ClientCredentialsResourceDetails();
    }

    @Bean
    public OAuth2RestTemplate localAuthServerRestTemplate() {
        return new OAuth2RestTemplate(localAuthServer()/*, oauth2ClientContext*/);
    }
}
