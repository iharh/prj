package mygwt.web.export;

import mygwt.common.adhoc.AdHocConstants;
import mygwt.common.exception.CMPException;

import mygwt.web.server.CmpRemoteServletSupport;

//import mygwt.common.i18n.ResUtils;
//import mygwt.web.customexport.server.CustomExportDataflowService;

//import mygwt.web.export.resources.ExpMsgKeys;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.SystemUtils;

import org.apache.commons.io.IOUtils;
//import org.springframework.web.bind.WebDataBinder;
//import org.springframework.web.bind.annotation.InitBinder;
//import java.beans.PropertyEditorSupport;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/export/*")
public class ExportController extends CmpRemoteServletSupport implements ExportService {
    private static final Logger log = LoggerFactory.getLogger(ExportController.class);

    private static final long serialVersionUID = 1L;
	
    private static final String TEST_FILE_NAME = SystemUtils.getUserHome() + File.separator + ".gitconfig"; // SystemUtils.IS_OS_LINUX

    @GetMapping("test")
    public ResponseEntity<String> test() {
        log.info("test called");
        return new ResponseEntity<String>("test called", HttpStatus.OK);
    }

    @Override
    @PostMapping(value = "latest_sentiment_exports")
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
            throw new CMPException(e.getMessage());
        }
        log.info("Simple downloadLatestSentimentExport successfully finished");
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

    /*
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(ReportSequence.class, new ReportSequenceConverter());
        binder.registerCustomEditor(ClassificationAlertInfo.class, new ClassificationAlertInfoConverter());
        binder.registerCustomEditor(Long[].class, "modelIds", new ModelIdsArrayConverter()); //$NON-NLS-1$
        binder.registerCustomEditor(TrendMetadata.class, new TrendMetadataConverter());
        binder.registerCustomEditor(List.class, "quadrantFiltersList", new ListLongConverter());
        binder.registerCustomEditor(WidgetDataRequest.class, "dataRequest", new WidgetDataRequestConverter());
        binder.registerCustomEditor(TimezoneInfo.class, new TimezoneInfoConverter());
    }*/
}
