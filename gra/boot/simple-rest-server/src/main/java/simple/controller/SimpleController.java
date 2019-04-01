package simple;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(produces = "application/json")
@Slf4j
public class SimpleController {

    @GetMapping("/hello")
    public String hello(HttpServletRequest request) throws Exception {
        StringBuilder result = new StringBuilder("headers: ");

        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            for (Enumeration<String> e = headerNames; e.hasMoreElements();) {
                String headerName = e.nextElement();
                result.append(",");
                result.append(headerName);
                result.append(":");
                result.append(request.getHeader(headerName));
            }
        }

        return result.toString();
    }
}
