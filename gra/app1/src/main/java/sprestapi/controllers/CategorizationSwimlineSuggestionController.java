package sprestapi.controllers;

import sprestapi.models.SuggestersListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * Created by Artyom.Gabeev on 9/9/15.
 */
@RestController
@RequestMapping("/v1/csls")
@Slf4j
public class CategorizationSwimlineSuggestionController extends ControllerBase {

    @RequestMapping(value = "/suggestersList", method = RequestMethod.GET)
    public SuggestersListResponse getSuggestersList() {
        return new SuggestersListResponse(Arrays.asList("s1", "s2"));
    }
}
