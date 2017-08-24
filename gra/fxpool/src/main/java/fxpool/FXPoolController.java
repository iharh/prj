package fxpool;

import org.springframework.context.annotation.Bean;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

//import org.springframework.stereotype.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;

//@Controller
@EnableAutoConfiguration
@RestController
@RequestMapping("/v1")
public class FXPoolController {

    @RequestMapping("/hello")
    @ResponseBody
    String hello() {
        return "os.name: " + System.getProperty("os.name")
            + " os.version: " + System.getProperty("os.version")
        ;
    }

    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();

        factory.setPort(18080);
        //factory.setSessionTimeout(10, TimeUnit.MINUTES);
        //factory.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/notfound.html"));

        return factory;
    }

    /*
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleError(HttpServletRequest request, Exception exception) {
        log.error("Exception during request processing: ", exception);
        HttpStatus statusCode;
        if (exception instanceof ServiceClientException) {
            statusCode = HttpStatus.BAD_REQUEST;
        } else {
            statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), statusCode);
    }

    @AllArgsConstructor
    public static class ErrorResponse {

        @Getter
        private String errorMessage;

    }
    */

    public static void main(String[] args) throws Exception {
        SpringApplication.run(FXPoolController.class, args);
    }
}
