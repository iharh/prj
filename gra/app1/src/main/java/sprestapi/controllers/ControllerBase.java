package sprestapi.controllers;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Artyom.Gabeev on 9/15/15.
 */
@Slf4j
public class ControllerBase {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleError(HttpServletRequest request, Exception exception) {
        log.error("Exception during request processing: ", exception);
        HttpStatus statusCode;
        if (exception instanceof IllegalArgumentException) {
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

}
