package mygwt.common.client.service;

import mygwt.portal.dto.SentimentUploadValidationResult;
import mygwt.portal.dto.sentiments.rse.RecentSentimentExportsInfo;

import mygwt.foundation.client.exception.ServiceException;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface SentimentTransferServiceAsync {
    // upload
    void getPreliminaryUploadResults(AsyncCallback<SentimentUploadValidationResult> callBack) throws ServiceException;
    void updateSentimentsWithUploadedData(long projectId, AsyncCallback<Void> callBack) throws ServiceException;
    void cleanupSentimentsWithUploadedData(long projectId, AsyncCallback<Void> callBack);
    // recent exports
    void getExports(long projectId, AsyncCallback<List<RecentSentimentExportsInfo>> callback);
}
