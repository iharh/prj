package mygwt.web.server;

import mygwt.common.adhoc.AdHocConstants;
import mygwt.common.client.service.RecentSentimentExportsService;
import mygwt.common.client.service.SentimentImportService;
import mygwt.portal.dto.SentimentUploadValidationResult;
import mygwt.portal.dto.sentiments.rse.RecentSentimentExportsInfo;
import mygwt.foundation.client.exception.ServiceException;

import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import org.apache.commons.io.IOUtils;

import java.util.List;
import java.util.Arrays;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// CmpRemoteServletSupport is quite big, so let's just extends AutoinjectingRemoteServiceServlet
@Controller
@RequestMapping("/sentiment_transfer_service/*")
public class SentimentTransferServiceImpl extends AutoinjectingRemoteServiceServlet implements SentimentImportService, RecentSentimentExportsService {
    private static final Logger log = LoggerFactory.getLogger(SentimentTransferServiceImpl.class);

    private static final String TEST_FILE_NAME = "file.txt";

    // SentimentImportService

    @GetMapping("test")
    public ResponseEntity<String> test() {
        log.info("test called");
        return new ResponseEntity<String>("test called", HttpStatus.OK);
    }

    //@PostMapping(value = "uploadfile")
    @RequestMapping(value = "uploadfile", method = RequestMethod.POST)
    public ResponseEntity<String> uploadFile(@RequestParam("upload") MultipartFile requestFile,
           //@RequestParam("projectId") Long projectId,
           HttpSession session
        ) {
        String responseMsg = AdHocConstants.UPLOAD_OK;
        try {
            log.info("uploading file: {}", requestFile.getOriginalFilename());
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

    @Override
    public SentimentUploadValidationResult getPreliminaryUploadResults() throws ServiceException {
        SentimentUploadValidationResult result = new SentimentUploadValidationResult();
        result.addSkippedWord(null);
        result.addSkippedRule(null);
        //result.setNegatorTuned(true);
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

    // RecentSentimentExportsService

    @Override
    public List<RecentSentimentExportsInfo> getExports(long projectId) /*throws ServiceException*/ {
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

    // ExportService

    //@PostMapping("latest_sentiment_exports")
    @RequestMapping(value = "latest_sentiment_exports", method = RequestMethod.POST)
    public ResponseEntity<String> downloadLatestSentimentExport(@RequestParam("projectId") long projectId, @RequestParam("exportId") String exportId, HttpServletResponse response) {
        log.info("Simple downloadLatestSentimentExport successfully started");
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
            // throw new CMPException(e.getMessage());
        }
        log.info("Simple downloadLatestSentimentExport successfully finished");
        return createResponse(responseMsg);
    }
}
