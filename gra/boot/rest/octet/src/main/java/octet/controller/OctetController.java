package octet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.apache.commons.io.IOUtils;

import java.io.InputStream;

import lombok.extern.slf4j.Slf4j;

import static java.nio.charset.StandardCharsets.UTF_8;

@RestController
@RequestMapping("/octet")
@Slf4j
public class OctetController {
    @CrossOrigin
    @GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity getResource() throws Exception {
        ResponseEntity<String> response = new ResponseEntity<>("abc", null, HttpStatus.OK);
        log.info("get called");
        return response;
    }

    @CrossOrigin
    @PostMapping(consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public ResponseEntity putResource(InputStream inputStream) throws Exception {
        String body = IOUtils.toString(inputStream, UTF_8.name());
        log.info("put called with body: {}", body);
        return ResponseEntity.ok().build();
    }
}
