package octet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

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
    public ResponseEntity putResource() throws Exception {
        log.info("put called");
        return ResponseEntity.ok().build();
    }
}
