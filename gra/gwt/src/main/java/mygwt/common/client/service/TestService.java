package mygwt.common.client.service;

//import mygwt.common.security.annotation.SecureParameter;
//import mygwt.common.security.model.ClassName;
//import mygwt.common.security.model.PermissionName;

// import com.google.gwt.user.client.rpc.RemoteService;

import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/sentiment_transfer_service/*")
public interface TestService { // extends RemoteService
    //@SecureParameter(permission = PermissionName.WRITE, strategyClass = ClassName.PROJECT)
    @RequestMapping(value = "test", method = RequestMethod.GET)
    ResponseEntity<String> test(); // throws ServiceException;
}
