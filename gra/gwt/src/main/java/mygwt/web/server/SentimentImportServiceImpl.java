package mygwt.web.server;

import mygwt.common.client.service.SentimentImportService;
import mygwt.portal.dto.SentimentUploadValidationResult;
import mygwt.foundation.client.exception.ServiceException;

import org.springframework.stereotype.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

// extends CmpRemoteServletSupport is quite big
@Controller
@RequestMapping("/sentiment_import_service/*")
public class SentimentImportServiceImpl extends AutoinjectingRemoteServiceServlet implements SentimentImportService {
    private static final Logger log = LoggerFactory.getLogger(SentimentImportServiceImpl.class);

    @GetMapping("test")
    public ResponseEntity<String> test() {
        log.info("test called");
        return new ResponseEntity<String>("test called", HttpStatus.OK);
    }

    @PostMapping("uploadfile")
    ResponseEntity<String> uploadFile(@RequestParam("upload") MultipartFile requestFile,
           @RequestParam("projectId") Long projectId
           //HttpSession session
        ) {
        log.info("uploadFile called");
        //log.info("uploading file: {}", requestFile.getOriginalFilename());
        return new ResponseEntity<String>("u", HttpStatus.OK);
    }

    @Override
    public SentimentUploadValidationResult getPreliminaryUploadResults() throws ServiceException {
        SentimentUploadValidationResult result = new SentimentUploadValidationResult();
        result.setNegatorTuned(true);
        log.info("get preliminary results}");
        return result;
    }

    @Override
    public void updateSentimentsWithUploadedData(long projectId) throws ServiceException {
        log.info("update sentiments for projectId: {}", projectId);
    }

    @Override
    public void cleanupSentimentsWithUploadedData(long projectId) {
        log.info("clean up sentiments for projectId: {}", projectId);
    }
}
