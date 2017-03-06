package mygwt.common.client.service;

//import mygwt.common.security.annotation.SecureParameter;
//import mygwt.common.security.model.ClassName;
//import mygwt.common.security.model.PermissionName;

import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

@Controller
public interface SentimentTransferRestService {
    //@SecureParameter(permission = PermissionName.WRITE, strategyClass = ClassName.PROJECT)
    @RequestMapping(value = "test", method = RequestMethod.GET)
    ResponseEntity<String> test(); // throws ServiceException;

    // ?? security
    @RequestMapping(value = "uploadfile", method = RequestMethod.POST)
    ResponseEntity<String> uploadFile(HttpSession httpSession
        , @RequestParam("upload") MultipartFile requestFile
        , @RequestParam("projectId") long projectId);

    // ?? security
    @RequestMapping(value = "recent_sentiment_exports", method = RequestMethod.POST)
    void downloadRecentSentimentExport(HttpServletResponse response
        , @RequestParam("projectId") long projectId
        , @RequestParam("exportId") String exportId);

    // ?? security
    @RequestMapping(value = "sentiment_export", method = RequestMethod.POST)
    void downloadCurrentSentimentExport(HttpServletResponse response
        , @RequestParam("projectId") long projectId
        , @RequestParam("exportRules") boolean exportRules
        , @RequestParam("exportWords") boolean exportWords
        , @RequestParam("exportName") String exportName
        , @RequestParam("exportDescription") String exportDescription);
}
