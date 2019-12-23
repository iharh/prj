package srv.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping(produces = "application/json")
public class SecureController {

    @GetMapping("/hello")
    public String hello() throws Exception {
        return "Hello World!";
    }
}
