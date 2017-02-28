package mygwt.common.client.service;

import mygwt.common.security.annotation.SecureParameter;
import mygwt.common.security.model.ClassName;
import mygwt.common.security.model.PermissionName;
import mygwt.portal.dto.SentimentUploadValidationResult;
import mygwt.foundation.client.exception.ServiceException;

import com.google.gwt.user.client.rpc.RemoteService;

public interface SentimentImportService extends RemoteService {
    SentimentUploadValidationResult getPreliminaryUploadResults() throws ServiceException;

    @SecureParameter(permission = PermissionName.WRITE, strategyClass = ClassName.PROJECT)
    void updateSentimentsWithUploadedData(long projectId) throws ServiceException;

    @SecureParameter(permission = PermissionName.WRITE, strategyClass = ClassName.PROJECT)
    void cleanupSentimentsWithUploadedData(long projectId);
}
