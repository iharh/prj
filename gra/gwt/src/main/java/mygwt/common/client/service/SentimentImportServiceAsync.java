package mygwt.common.client.service;

import mygwt.foundation.client.exception.ServiceException;
import mygwt.portal.dto.SentimentUploadValidationResult;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SentimentImportServiceAsync {
    void getPreliminaryUploadResults(AsyncCallback<SentimentUploadValidationResult> callBack) throws ServiceException;
    void updateSentimentsWithUploadedData(long projectId, AsyncCallback<Void> callBack) throws ServiceException;
    void cleanupSentimentsWithUploadedData(long projectId, AsyncCallback<Void> callBack);
}
