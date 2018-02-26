package cl;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;

@EnableAutoConfiguration
@EnableFeignClients
@Slf4j
public class Main implements CommandLineRunner {

    @FeignClient(name = "fxresource", url="http://epbygomw0024.gomel.epam.com:18080")
    public interface FXResourceService { 
	@RequestMapping(value = "/mobile/rest/fxresources/lexicons/export/{accountId}/{lexiconType}", method = RequestMethod.GET)
        // ResponseEntity<byte[]>
        ResponseEntity<String> exportLexicon(
            @PathVariable("accountId") long accountId,
            @PathVariable("lexiconType") String lexiconType
        );
    }

    @Autowired
    private FXResourceService client;

    @Override
    public void run(String... args) throws Exception {
        log.info("start");
        ResponseEntity<String> response = client.exportLexicon(0, "Custom");
        log.info("Got feign response: {}", response);
    }
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
