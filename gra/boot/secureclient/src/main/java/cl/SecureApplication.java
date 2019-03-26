package cl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

//@SpringBootApplication
@ComponentScan({"cl"})
public class SecureApplication implements CommandLineRunner {
    @Autowired
    OAuth2RestTemplate localT;

    @Override
    public void run(String... args) throws Exception {
        if (localT == null) {
            System.out.println("null localT");
        } else {
            OAuth2AccessToken accessToken = localT.getAccessToken();
            System.out.println("obtained access token: " + accessToken.getValue());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(SecureApplication.class, args);
    }
}
