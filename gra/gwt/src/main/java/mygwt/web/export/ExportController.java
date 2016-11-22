package mygwt.web.export;

import com.clarabridge.web.server.CmpRemoteServletSupport;

import com.clarabridge.common.dto.Project;
import com.clarabridge.common.exception.CMPException;
import com.clarabridge.common.i18n.ResUtils;
import com.clarabridge.foundation.client.exception.AccessDeniedClientException;
import com.clarabridge.web.customexport.server.CustomExportDataflowService;

import mygwt.web.export.resources.ExpMsgKeys;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
//import org.springframework.web.bind.WebDataBinder;
//import org.springframework.web.bind.annotation.InitBinder;
//import java.beans.PropertyEditorSupport;
//import com.clarabridge.common.date.TimezoneInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
//import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.Charset;
//import java.util.List;

import org.apache.log4j.Logger;

public class ExportController extends CmpRemoteServletSupport implements ExportService {
    private static final Logger LOG = Logger.getLogger(ExportController.class);

    private static final long serialVersionUID = 1L;
	
    private CustomExportDataflowService customExportDataflowService;
    
    public void setCustomExportDataflowService(CustomExportDataflowService customExportDataflowService) {
        this.customExportDataflowService = customExportDataflowService;
    }

    public void downloadCustomExport(final long projectId, long sessionId, final HttpServletResponse response) {       
        try {
            boolean hasAccess = getSecurityService().hasAccess(projectId, "PROJECT", false, "READ");
            if (hasAccess) {        
                String cextFileName = customExportDataflowService.getFileNameByDataflowSessionId(projectId, sessionId);
                response.setContentType("application/octet-stream"); //$NON-NLS-1$
                response.setHeader("Content-Disposition", "attachment; filename=\"" //$NON-NLS-1$ //$NON-NLS-2$
                        + URLEncoder.encode(cextFileName, "UTF-8") + "\"");
                response.setCharacterEncoding("UTF-8");
                OutputStream out = response.getOutputStream();
                File expFile = new File(System.getProperty("customExport.targetDir") + "/" +  projectId + "/"
                        + cextFileName);
                out.write(IOUtils.toByteArray(new FileInputStream(expFile)));
                
            } else {
                String msg = ResUtils.getMessage(ExpMsgKeys.EXPORT_MSG_BUNDLE, ExpMsgKeys.CALLBACK_ERR_ACCESS_DENIED,
                        String.valueOf(projectId));             
                throw new AccessDeniedClientException(msg);
            }
        } catch (Throwable e) { // which is ClientAbortException also
            try {
                LOG.error("Internal error.", e);  // just log and swallow //$NON-NLS-1$
                try {
                    if (!response.isCommitted()) {
                        response.reset(); // try to clear response    
                    }
                    // if response has already been committed, we can't reset headers so the part of the file
                    // was transferred, in case of XLS and PDF files might be corrupted.
                } finally {
                    ServletOutputStream stream = response.getOutputStream();
                    PrintWriter writer = new PrintWriter(new OutputStreamWriter(stream,
                                    Charset.forName("UTF-8"))); //$NON-NLS-1$
                    String msg = ResUtils.getMessage(ExpMsgKeys.EXPORT_MSG_BUNDLE, ExpMsgKeys.CALLBACK_ERR_SAVE_REPORT,
                                    e.getMessage());
                    writer.write(msg);
                    writer.flush();
                }
            } catch (Throwable e2) { // IllegalStateException if the response is committed, or IOException 
                LOG.error(e2);
            }
        }
    }
	
    public void exportSharedLexicon(ExportSharedLexiconProperties properties, HttpServletResponse response) {
        //final InputStream input = fxSharedResourcesService.openSharedResource(properties.getSlType(), "lexicon",
        //    new SearchCriteria(Type.ACCOUNT, properties.getAccountId()));
        try {
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + properties.getSlType() + ".dct\"");
            IOUtils.copy(input, response.getOutputStream());
            input.close();
        } catch (IOException e) {
            IOUtils.closeQuietly(input);
            LOG.error(e);
            throw new CMPException(e.getMessage());
        }
        LOG.info("Hierarchy export successfully finished"); //$NON-NLS-1$
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
