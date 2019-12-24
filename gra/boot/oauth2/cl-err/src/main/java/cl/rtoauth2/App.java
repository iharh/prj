package cl.rtoauth2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.security.oauth2.client.OAuth2RestTemplate;
// import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class App implements CommandLineRunner {
    @Autowired
    // private OAuth2RestTemplate restTemplate;
    private RestTemplate restTemplate;
    
    public String getGreeting() {
        return "Hello world.";
    }

    private String buildUrl(String path) {
        // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/util/UriComponentsBuilder.html
        return UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("localhost")
                // 8023
                .port(18080) 
                .path(path)
                .build()
                .toUriString();
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("greeting: {}", getGreeting());

        // OAuth2AccessToken accessToken = restTemplate.getAccessToken();
        // log.info("obtained access token: {}", accessToken.getValue());

        //final String url = buildUrl("hello");
        final String url = buildUrl("mobile/rest/fxresources/lexicons/nlpsvc/export/0/product");

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        log.info("response status code: {}", response.getStatusCode()); // HttpStatus.OK
        if (HttpStatus.OK == response.getStatusCode()) {
            log.info("response body: {}", response.getBody());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
