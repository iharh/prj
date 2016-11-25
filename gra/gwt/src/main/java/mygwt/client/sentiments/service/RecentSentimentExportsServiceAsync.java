package mygwt.client.sentiments.service;

import mygwt.client.sentiments.RecentSentimentExportsInfo;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface RecentSentimentExportsServiceAsync {
    void getExports(long projectId, AsyncCallback<List<RecentSentimentExportsInfo>> callback);
}
