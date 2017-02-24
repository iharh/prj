package mygwt.common.client.service;

import mygwt.common.security.annotation.SecureParameter;
import mygwt.common.security.model.PermissionName;
import mygwt.portal.dto.SentimentUploadValidationResult;
import mygwt.foundation.client.exception.ServiceException;

//import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;

import com.google.gwt.user.client.rpc.RemoteService;

public interface SentimentImportService extends RemoteService {
    SentimentUploadValidationResult getPreliminaryUploadResults() throws ServiceException;

    @SecureParameter(permission = PermissionName.WRITE, strategyName = "PROJECT")
    void updateSentimentsWithUploadedData(long projectId) throws ServiceException;

    @SecureParameter(permission = PermissionName.WRITE, strategyName = "PROJECT")
    void cleanupSentimentsWithUploadedData(long projectId);
}
