package mygwt.common.client.service;

import mygwt.common.security.model.ClassName;
import mygwt.common.security.model.PermissionName;
import mygwt.common.security.annotation.SecureParameter;

import mygwt.portal.dto.SentimentUploadValidationResult;
import mygwt.portal.dto.sentiments.rse.RecentSentimentExportsInfo;

import mygwt.foundation.client.exception.ServiceException;

import com.google.gwt.user.client.rpc.RemoteService;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping("/sentiment_transfer_service/*")
public interface SentimentTransferService extends RemoteService {
    // sentiment import
    SentimentUploadValidationResult getPreliminaryUploadResults() throws ServiceException;

    @SecureParameter(permission = PermissionName.WRITE, strategyClass = ClassName.PROJECT)
    void updateSentimentsWithUploadedData(long projectId) throws ServiceException;

    @SecureParameter(permission = PermissionName.WRITE, strategyClass = ClassName.PROJECT)
    void cleanupSentimentsWithUploadedData(long projectId);
    // recent sentiments export
    @SecureParameter(permission = PermissionName.READ, strategyClass = ClassName.SENTIMENTS)
    List<RecentSentimentExportsInfo> getExports(long projectId); // throws ServiceException;
}
