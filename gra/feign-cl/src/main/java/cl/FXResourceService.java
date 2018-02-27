package cl;

import org.springframework.cloud.netflix.feign.FeignClient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="fxresource", url="${cmp.url}")
public interface FXResourceService { 
    @RequestMapping(value = "/mobile/rest/fxresources/lexicons/export/{accountId}/{lexiconType}", method = RequestMethod.GET)
    ResponseEntity<String> exportLexicon(
        @PathVariable("accountId") long accountId,
        @PathVariable("lexiconType") String lexiconType
    );
}
