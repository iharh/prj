package cl.controller;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.security.oauth2.client.OAuth2RestTemplate;

@RestController
@RequestMapping("/v1")
public class SecureController {
    @Autowired
    OAuth2RestTemplate localT;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello() {
        return "hello" + localT;
    }
}
