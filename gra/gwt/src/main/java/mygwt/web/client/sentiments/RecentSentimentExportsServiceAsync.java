package mygwt.web.client.sentiments;

import com.google.gwt.user.client.rpc.AsyncCallback;

//import java.util.List;

public interface RecentSentimentExportsServiceAsync {
    void getExports(long projectId, AsyncCallback<String/*List<RecentSentimentExportsInfo>*/> callback);
}
