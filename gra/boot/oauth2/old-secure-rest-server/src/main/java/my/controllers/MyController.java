package my.controllers;

import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/v1")
public class MyController {

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @PreAuthorize("#oauth2.hasScope('cmp.lexicons.export')")
    public String hello() {
        return "hello";
    }
}
