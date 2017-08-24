package fxpool;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

//import org.springframework.stereotype.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

//@Controller
@EnableAutoConfiguration
@RestController
@RequestMapping("/v1")
public class FXPoolController {

    @RequestMapping("/hello")
    @ResponseBody
    String hello() {
        return "Hello World!";
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
