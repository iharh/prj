package cl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import lombok.extern.slf4j.Slf4j;

//@SpringBootApplication
@ComponentScan({"cl", "cl.config"})
@Slf4j
public class SecureApplication implements CommandLineRunner {
    @Autowired
    OAuth2RestTemplate localT;

    @Override
    public void run(String... args) throws Exception {
        if (localT == null) {
            log.info("null localT");
        } else {
            log.info("non null localT");
            OAuth2AccessToken accessToken = localT.getAccessToken();
            log.info("obtained access token: {}", accessToken.getValue());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(SecureApplication.class, args);
    }
}
