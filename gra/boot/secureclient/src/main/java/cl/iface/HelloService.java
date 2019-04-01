package cl.iface;

import cl.config.OAuth2ClientConfig;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="helloresource", url="http://localhost:8080", configuration=OAuth2ClientConfig.class)
public interface HelloService { 
    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    ResponseEntity<String> hello();
}
