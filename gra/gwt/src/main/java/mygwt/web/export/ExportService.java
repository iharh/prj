package mygwt.web.export;

import mygwt.common.security.annotation.SecureParameter;
import mygwt.common.security.model.ClassName;
import mygwt.common.security.model.PermissionName;

//import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

//import org.springframework.web.bind.WebDataBinder;
//import org.springframework.web.bind.annotation.InitBinder;
//import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.http.ResponseEntity;

public interface ExportService {
    @SecureParameter(permission = PermissionName.READ, strategyClass = ClassName.SENTIMENTS)
    ResponseEntity<String> downloadLatestSentimentExport(long projectId, String exportId, HttpServletResponse response);

    //@InitBinder
    //void initBinder(WebDataBinder binder);
}
