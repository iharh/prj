package mygwt.web.server.security;
/*
//import mygwt.common.dataflow.DataflowService;
//import mygwt.common.dto.DataflowSession;
import mygwt.common.exception.ServerException;
import mygwt.common.i18n.ResUtils;
//import mygwt.common.security.spring.SecurityContext;
import mygwt.common.security.annotation.SecureParameter;
import mygwt.common.security.model.ClassName;
import mygwt.common.security.model.PermissionName;
//import mygwt.common.security.service.SecurityService;

//import mygwt.portal.treeaudit.AudtiProperties;
//import mygwt.reports.metadata.ExportReportRunProperties;
//import mygwt.web.export.AccountTranDetailsProperties;
//import mygwt.web.export.ExportEventLogProperties;
//import mygwt.web.export.ExportHierarchyProperties;
//import mygwt.web.export.ExportModelProperties;
//import mygwt.web.export.ExportSharedLexiconProperties;
import mygwt.web.export.resources.ExpMsgKeys;
*/
//import org.springframework.security.access.AccessDeniedException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
//import org.apache.commons.lang.ArrayUtils;
//import org.apache.commons.lang.StringEscapeUtils;
/*
import javax.servlet.ServletResponse;
import javax.servlet.ServletOutputStream;

import java.io.PrintWriter;
import java.io.OutputStreamWriter;
*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import static java.nio.charset.StandardCharsets.*;

public class SentimentTransferSecurityInterceptor implements MethodInterceptor {
    private static final Logger log = LoggerFactory.getLogger(SentimentTransferSecurityInterceptor.class);
/*

    private SecurityService securityService;
    private DataflowService dataflowService;

    public void setSecurityService(SecurityService securityService) {
        this.securityService = securityService;
    }
    public void setDataflowService(DataflowService dataflowService) {
        this.dataflowService = dataflowService;
    }
*/
    @Override
    public Object invoke(MethodInvocation methodInvocation) throws Throwable {
        log.info("intercept method [{}]", methodInvocation.getMethod());
        //try {
            //check(methodInvocation);
            return methodInvocation.proceed();
        /*} catch (Exception ex) {
            log.error("Export failed!", ex);
            ServletResponse response = getResponse(methodInvocation);
            if (response != null) {
                outputErrorMessage(response, ex);
                return null;
            } else {
                throw ex;
            }
        }*/
    }
    
    /*private ServletResponse getResponse(MethodInvocation methodInvocation) {
        for (Object object : methodInvocation.getArguments()) {
            if (object instanceof ServletResponse) {
                return (ServletResponse) object;
            }
        }
        return null;
    }
    
    private void outputErrorMessage(ServletResponse response, Exception exception) throws Exception {
        try {
            response.setContentType("text/html");
            ServletOutputStream stream = response.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(stream, UTF_8));
            String msg = ResUtils.getMessage(ExpMsgKeys.EXPORT_MSG_BUNDLE, ExpMsgKeys.CALLBACK_ERR_ERR_EXP, exception.getMessage());
            writer.write(msg);
            writer.flush();
        } catch (Exception ex) {
            log.warn("Failed to output error message: ", ex);
            throw exception;
        }
    }

    private void check(MethodInvocation methodInvocation) throws ServerException {
        if (methodInvocation.getMethod().getAnnotation(SecureParameter.class) != null) {
            SecureParameter parameter = methodInvocation.getMethod().getAnnotation(SecureParameter.class);
            Object securedObject = methodInvocation.getArguments()[parameter.num()];
            final ClassName strategyClass = parameter.strategyClass();
            final String strategyName = strategyClass == ClassName.NONE ? parameter.strategyName() : strategyClass.name();
            // if (!securityService.hasAccess(securedObject, strategyName, parameter.isRecursiveOnly(), parameter.isNullPermissed(), parameter.permission().name())) {
            //    log.warn("There was an attempt to access object [{}] by user [{}] using method [{}] without appropriate permission", securedObject, SecurityContext.getUser(), methodInvocation.getMethod());
            //    throw new AccessDeniedException(ResUtils.getMessage(ExpMsgKeys.EXPORT_MSG_BUNDLE, ExpMsgKeys.CALLBACK_ERR_ACCESS_DENIED, securedObject));
            //}
        }
    }
    */
}
