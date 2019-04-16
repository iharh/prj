package cl;

import cl.iface.HelloService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// import org.springframework.security.oauth2.client.OAuth2RestTemplate;
// import org.springframework.security.oauth2.common.OAuth2AccessToken;

import org.springframework.cloud.openfeign.EnableFeignClients;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableFeignClients
@Slf4j
public class SecureApplication implements CommandLineRunner {
    //@Autowired
    //private OAuth2RestTemplate localT;

    @Autowired
    private HelloService helloService;
    
    @Override
    public void run(String... args) throws Exception {
        //OAuth2AccessToken accessToken = localT.getAccessToken();
        //log.info("obtained access token: {}", accessToken.getValue());

        ResponseEntity<String> helloServiceResponse = helloService.hello();
        String helloServiceResponseBody = helloServiceResponse.getBody();

        log.info("hello service response body {}", helloServiceResponseBody);
    }

    public static void main(String[] args) {
        SpringApplication.run(SecureApplication.class, args);
    }
}
