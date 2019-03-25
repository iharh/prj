package cl;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;

import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

//@ComponentScan({"cl"})
@EnableOAuth2Client
public class Main implements CommandLineRunner {
    /*
    @Autowired
    OAuth2ClientContext oauth2ClientContext;

    @Bean
    @ConfigurationProperties("localas.client")
    public AuthorizationCodeResourceDetails localAuthServer() {
        return new AuthorizationCodeResourceDetails();
    }
    */

    @Override
    public void run(String... args) throws Exception {
        System.out.println("starting ...");
        // OAuth2RestTemplate localAuthServerTemplate = new OAuth2RestTemplate(localAuthServer(), oauth2ClientContext);
        System.out.println("done.");
        
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
