package mygwt.web.server;

import mygwt.common.adhoc.AdHocConstants;
import mygwt.common.client.service.SentimentTransferService;
import mygwt.common.client.service.SentimentTransferRestService;
import mygwt.portal.dto.SentimentUploadConstants;
import mygwt.portal.dto.SentimentUploadValidationResult;
import mygwt.portal.dto.sentiments.rse.RecentSentimentExportsInfo;
import mygwt.foundation.client.exception.ServiceException;

import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.io.IOUtils;

import java.util.List;
import java.util.Arrays;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// CmpRemoteServletSupport is quite big, so let's just extends AutoinjectingRemoteServiceServlet
public class SentimentTransferServiceImpl extends AutoinjectingRemoteServiceServlet implements SentimentTransferService, SentimentTransferRestService {
    private static final Logger log = LoggerFactory.getLogger(SentimentTransferServiceImpl.class);

    private static final String SENTIMENT_UPLOAD_RESULT_IS_NOT_FOUND_IN_THE_SESSION_DATA = "Sentiment upload result is not found in the session data.";

    private static final String TEST_FILE_NAME = "readme.txt";

    @Override
    public ResponseEntity<String> test() {
        log.info("test called");
        return new ResponseEntity<String>("test called", HttpStatus.OK);
    }
    // sentiment import

    @Override
    public ResponseEntity<String> uploadFile(HttpSession httpSession
        , @RequestParam("upload") MultipartFile requestFile
        , @RequestParam("projectId") long projectId
        ) {
        String responseMsg = AdHocConstants.UPLOAD_OK;
        try {
	    String sessionId = httpSession.getId();
            log.info("uploading file: {} sessionId: {} started", requestFile.getOriginalFilename(), sessionId);

            SentimentUploadValidationResult result = makeSentimentUploadValidationResult(projectId, sessionId, requestFile);
            httpSession.setAttribute(SentimentUploadConstants.SENTIMENT_UPLOAD_RESULT_KEY, result);

            log.info("uploading file: {} finished", requestFile.getOriginalFilename());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            responseMsg = e.getMessage();
        }
        return createResponse(responseMsg);
    }

    private ResponseEntity<String> createResponse(final String message) {
        final StringBuffer buf = new StringBuffer();
        buf.append(AdHocConstants.UPLOAD_RESULT_S);
        buf.append(message);
        buf.append(AdHocConstants.UPLOAD_RESULT_E);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        return new ResponseEntity<String>(buf.toString(), headers, HttpStatus.OK);
    }

    private SentimentUploadValidationResult makeSentimentUploadValidationResult(long projectId, String sessionId, MultipartFile requestFile) {
    	//SentimentUploadValidationResult result = cmpService.uploadSentimentFile(projectId, sessionId, file);

        SentimentUploadValidationResult result = new SentimentUploadValidationResult(projectId, sessionId);
        result.addSkippedWord(null);
        result.addSkippedRule(null);
        //result.setSessionId("session1");
        //result.setNegatorTuned(true);

        return result;
    }

    @Override
    public SentimentUploadValidationResult getPreliminaryUploadResults() throws ServiceException {
        SentimentUploadValidationResult result = null;
        log.info("get preliminary results started");

        HttpServletRequest req = getThreadLocalRequest();
        HttpSession httpSession = req.getSession();

        Object objResult = httpSession.getAttribute(SentimentUploadConstants.SENTIMENT_UPLOAD_RESULT_KEY);
        if (objResult != null) {
            result = (SentimentUploadValidationResult) objResult;
        }
        String sessionId = httpSession.getId();
        log.info("get preliminary results sessionId: {} result: {} finished", sessionId, (result == null ? "<null>" : "<non-null>"));
        return result;
    }

    @Override
    public void updateSentimentsWithUploadedData(long projectId) throws ServiceException {
        log.info("update sentiments for projectId: {} started", projectId);

        HttpServletRequest req = getThreadLocalRequest();
        HttpSession httpSession = req.getSession();
        Object objResult = httpSession.getAttribute(SentimentUploadConstants.SENTIMENT_UPLOAD_RESULT_KEY);

        if (objResult != null) {
            String sessionId = httpSession.getId();
            //cmpService.updateSentimentsWithUploadedData(projectId, sessionId);
            log.info("update sentiments for projectId: {} sessionId: {} finished", projectId, sessionId);
        } else {
            log.error(SENTIMENT_UPLOAD_RESULT_IS_NOT_FOUND_IN_THE_SESSION_DATA);
            throw new ServiceException(SENTIMENT_UPLOAD_RESULT_IS_NOT_FOUND_IN_THE_SESSION_DATA);
        }
    }

    @Override
    public void cleanupSentimentsWithUploadedData(long projectId) {
        log.info("clean up sentiments for projectId: {} started", projectId);

        HttpServletRequest req = getThreadLocalRequest();
        HttpSession httpSession = req.getSession();
	String sessionId;

        Object objResult = httpSession.getAttribute(SentimentUploadConstants.SENTIMENT_UPLOAD_RESULT_KEY);

        if (objResult != null) {
            SentimentUploadValidationResult result = (SentimentUploadValidationResult) objResult;
            sessionId = result.getSessionId();
	    // cmpService.cleanupSentimentsWithUploadedData(projectId, sessionId);
            httpSession.removeAttribute(SentimentUploadConstants.SENTIMENT_UPLOAD_RESULT_KEY);
        } else {
	    sessionId = httpSession.getId();
        }

        log.info("clean up sentiments for projectId: {} sessionId: {} finished", projectId, sessionId);
    }

    // recent sentiment exports

    @Override
    public List<RecentSentimentExportsInfo> getExports(long projectId) { // throws ServiceException
        log.info("getExports called");
        List<RecentSentimentExportsInfo> result = null;
        //try {
            result = Arrays.asList(
                new RecentSentimentExportsInfo("export0", "descr0", "2016/05/19", false, false),
                new RecentSentimentExportsInfo("export1", "descr1", "2016/05/19", false, true),
                new RecentSentimentExportsInfo("export2", "descr2", "2016/05/19", true, false),
                new RecentSentimentExportsInfo("export3", "descr3", "2016/05/19", true, true),
                new RecentSentimentExportsInfo("export4", "descr4", "2016/05/19", false, false),
                new RecentSentimentExportsInfo("export5", "descr5", "2016/05/19", false, false),
                new RecentSentimentExportsInfo("export6", "descr6", "2016/05/19", false, false),
                new RecentSentimentExportsInfo("export7", "descr7", "2016/05/19", false, false),
                new RecentSentimentExportsInfo("export8", "descr8", "2016/05/19", false, false),
                new RecentSentimentExportsInfo("export9", "descr9", "2016/05/19", false, false)
            );
        //} catch (IOException e) {
        //    IOUtils.closeQuietly(input);
        //    LOG.error(e);
        //    throw new ServiceException(e.getMessage());
        //} 
        return result;
    }

    @Override
    public void downloadRecentSentimentExport(HttpServletResponse response
        , @RequestParam("projectId") long projectId
        , @RequestParam("exportId") String exportId
        ) {
        log.info("downloadRecentSentimentExport started");
        InputStream input = null;
        String responseMsg = AdHocConstants.UPLOAD_OK;
        try {
            input = new FileInputStream(new File(TEST_FILE_NAME));
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"file.dct\"");
            IOUtils.copy(input, response.getOutputStream());
            input.close();
        } catch (IOException e) {
            responseMsg = e.getMessage();
            IOUtils.closeQuietly(input);
            log.error(e.getMessage(), e);
        }
        log.info("downloadRecentSentimentExport finished");
    }

    // ExportService

    @Override
    public void downloadCurrentSentimentExport(HttpServletResponse response
        , @RequestParam("projectId") long projectId
        , @RequestParam("exportRules") boolean exportRules
        , @RequestParam("exportWords") boolean exportWords
        , @RequestParam("exportName") String exportName
        , @RequestParam("exportDescription") String exportDescription
        ) {

        log.info("downloadCurrentSentimentExport started");
        InputStream input = null;
        String responseMsg = AdHocConstants.UPLOAD_OK;
        try {
            input = new FileInputStream(new File(TEST_FILE_NAME));
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"file.dct\"");
            IOUtils.copy(input, response.getOutputStream());
            input.close();
        } catch (IOException e) {
            responseMsg = e.getMessage();
            IOUtils.closeQuietly(input);
            log.error(e.getMessage(), e);
        }
        log.info("downloadCurrentSentimentExport finished");
    }
}
