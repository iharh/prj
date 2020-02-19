package cl.resrep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
// import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class ResRepApp implements CommandLineRunner {
    @Autowired
    private OAuth2RestTemplate restTemplate;
    // private RestTemplate restTemplate;
    
    public String getGreeting() {
        return "Hello world.";
    }

    private String buildUrl(String path) {
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                .port(8082) 
                .path(path)
                .build()
                .toUriString();
    }

    @Override
    public void run(String... args) throws Exception {
        OAuth2AccessToken accessToken = restTemplate.getAccessToken();
        log.info("obtained access token: {}", accessToken.getValue());

        final String url = buildUrl("v1/resource");

        // ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        // log.info("response status code: {}", response.getStatusCode()); // HttpStatus.OK
        // if (HttpStatus.OK == response.getStatusCode()) {
        //     log.info("response body: {}", response.getBody());
        //}
    }

    public static void main(String[] args) {
        SpringApplication.run(ResRepApp.class, args);
    }
}
