package mygwt.web.client.sentiments.rse;

import mygwt.portal.dto.sentiments.rse.RecentSentimentExportsInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface RecentSentimentExportsServiceAsync {
    void getExports(long projectId, AsyncCallback<List<RecentSentimentExportsInfo>> callback);
}
