package mygwt.web.server.security;

//import mygwt.common.dataflow.DataflowService;
//import mygwt.common.dto.DataflowSession;
import mygwt.common.exception.ServerException;
import mygwt.common.i18n.ResUtils;
//import mygwt.common.security.spring.SecurityContext;
import mygwt.common.security.annotation.SecureParameter;
import mygwt.common.security.model.ClassName;
import mygwt.common.security.model.PermissionName;
//import mygwt.common.security.service.SecurityService;
/*
import mygwt.portal.treeaudit.AudtiProperties;
import mygwt.reports.metadata.ExportReportRunProperties;
import mygwt.web.export.AccountTranDetailsProperties;
import mygwt.web.export.ExportEventLogProperties;
import mygwt.web.export.ExportHierarchyProperties;
import mygwt.web.export.ExportModelProperties;
import mygwt.web.export.ExportSharedLexiconProperties;
*/
import mygwt.web.export.resources.ExpMsgKeys;

//import org.springframework.security.access.AccessDeniedException;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringEscapeUtils;

import javax.servlet.ServletResponse;
import javax.servlet.ServletOutputStream;

import java.io.PrintWriter;
import java.io.OutputStreamWriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.nio.charset.StandardCharsets.*;

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
        log.info("Intercept method [{}]", methodInvocation.getMethod());
        try {
            check(methodInvocation);
            return methodInvocation.proceed();
        } catch (Exception ex) {
            log.error("Export failed!", ex);
            ServletResponse response = getResponse(methodInvocation);
            if (response != null) {
                outputErrorMessage(response, ex);
                return null;
            } else {
                throw ex;
            }
        }
    }
    
    private ServletResponse getResponse(MethodInvocation methodInvocation) {
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
            /* if (!securityService.hasAccess(securedObject, strategyName, parameter.isRecursiveOnly(), parameter.isNullPermissed(), parameter.permission().name())) {
                log.warn("There was an attempt to access object [{}] by user [{}] using method [{}] without appropriate permission", securedObject, SecurityContext.getUser(), methodInvocation.getMethod());
                throw new AccessDeniedException(ResUtils.getMessage(ExpMsgKeys.EXPORT_MSG_BUNDLE, ExpMsgKeys.CALLBACK_ERR_ACCESS_DENIED, securedObject));
            }*/
        }
        /*for (Object arg : methodInvocation.getArguments()) {
            if (arg == null) {
                continue;
            } else if (arg instanceof AccountTranDetailsProperties) {
                checkAccountTranDetailsProperties((AccountTranDetailsProperties) arg);
            } else if (arg instanceof ExportEventLogProperties) {
                checkExportEventLogProperties((ExportEventLogProperties) arg);
            } else if (arg instanceof AudtiProperties) {
                checkAuditProperties((AudtiProperties) arg);
            } else if (arg instanceof ExportSharedLexiconProperties) {
                checkExportSharedLexiconProperties((ExportSharedLexiconProperties) arg);
            } else if (arg instanceof ExportHierarchyProperties) {
                checkExportHierarchyProperties((ExportHierarchyProperties) arg);
            } else if (arg instanceof ExportModelProperties) {
                checkExportModelProperties((ExportModelProperties) arg);
            } else if (arg instanceof ExportReportRunProperties) {
                checExportReportRunProperties((ExportReportRunProperties) arg);
            }
        }
        */
    }
/*
    private void checkAccountTranDetailsProperties(AccountTranDetailsProperties properties) {
        checkAccountAdminAccess(properties.getDataRequest().getParentId());
    }

    private void checkExportEventLogProperties(ExportEventLogProperties properties) throws ServerException {
        DataflowSession dataflowSession = dataflowService.getDataflowSessionById(properties.getSessionId());
        checkProjectAccess(dataflowSession.getProjectId());
    }

    private void checkAuditProperties(AudtiProperties properties) {
        checkModelAccess(properties.getModelId());
        properties.setClassfnAuditName(properties.getClassfnAuditName() != null ? StringEscapeUtils.escapeHtml(properties.getClassfnAuditName()) : "model");
    }

    private void checkExportSharedLexiconProperties(ExportSharedLexiconProperties properties) {
        checkAccountAdminAccess(properties.getAccountId());
        properties.setSlType(properties.getSlType() != null ? StringEscapeUtils.escapeHtml(properties.getSlType()) : null);
    }

    private void checkExportHierarchyProperties(ExportHierarchyProperties properties) {
        checkAccountAdminAccess(properties.getAccountId());
    }

    private void checkExportModelProperties(ExportModelProperties properties) {
        checkModelAccess(properties.getModelId());
        properties.setName(properties.getName() != null ? StringEscapeUtils.escapeHtml(properties.getName()) : "model");
        properties.setExportTo(properties.getExportTo() != null ? StringEscapeUtils.escapeHtml(properties.getExportTo()) : "xml");
    }

    private void checExportReportRunProperties(ExportReportRunProperties properties) {
        checkProjectAccess(properties.getProjectId());
        checkModelAccess(properties.getModelId());
        if (ArrayUtils.isNotEmpty(properties.getModelIds())) {
            for (Long modelId : properties.getModelIds()) {
                if (modelId != null) {
                    checkModelAccess(modelId);
                }
            }
        }
    }

    private void checkModelAccess(long modelId) {
        if (modelId > 0 && !securityService.hasAccess(modelId, ClassName.MODEL.name(), false, PermissionName.READ.name())) {
            LOG.warn("There was an attempt to access model [{}] by user [{}] without appropriate permission", modelId, SecurityContext.getUser());
            throw new AccessDeniedException(ResUtils.getMessage(ExpMsgKeys.EXPORT_MSG_BUNDLE, ExpMsgKeys.CALLBACK_ERR_MODEL_ACCESS_DENIED, String.valueOf(modelId)));
        }
    }

    private void checkProjectAccess(long projectId) {
        if (projectId > 0 && !securityService.hasAccess(projectId, ClassName.PROJECT.name(), false, PermissionName.READ.name())) {
            LOG.warn("There was an attempt to access project [{}] by user [{}] without appropriate permission", projectId, SecurityContext.getUser());
            throw new AccessDeniedException(ResUtils.getMessage(ExpMsgKeys.EXPORT_MSG_BUNDLE, ExpMsgKeys.CALLBACK_ERR_ACCESS_DENIED, String.valueOf(projectId)));
        }
    }

    private void checkAccountAdminAccess(long accountId) {
        if (accountId >= 0 && !securityService.isAdmin() && !(SecurityContext.getUser().isAccountAdmin() && SecurityContext.getUser().getAccountId().equals(accountId))) {
            LOG.warn("There was an attempt to access account [{}] by user [{}] without appropriate permission", accountId, SecurityContext.getUser());
            throw new AccessDeniedException(ResUtils.getMessage(ExpMsgKeys.EXPORT_MSG_BUNDLE, ExpMsgKeys.CALLBACK_ERR_ACCOUNT_ACCESS_DENIED, String.valueOf(accountId)));
        }
    }
*/
}
