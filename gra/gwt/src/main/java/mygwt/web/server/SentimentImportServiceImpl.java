package mygwt.web.server;

import mygwt.common.client.service.SentimentImportService;
import mygwt.portal.dto.SentimentUploadValidationResult;
import mygwt.foundation.client.exception.ServiceException;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;

// extends CmpRemoteServletSupport is quite big
public class SentimentImportServiceImpl extends AutoinjectingRemoteServiceServlet implements SentimentImportService {
    private static final Logger log = LoggerFactory.getLogger(SentimentImportServiceImpl.class);

    @PostMapping("uploadfile")
    void uploadFile(/*@RequestParam("upload") MultipartFile requestFile*/
           @RequestParam("projectId") Long projectId, HttpSession session) {
        log.info("projectId: {}", projectId);
        //log.info("uploading file: {}", requestFile.getOriginalFilename());
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
