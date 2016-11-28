package mygwt.web.client.sentiments;

// import mygwt.foundation.client.exception.ServiceException;

import com.google.gwt.user.client.rpc.RemoteService;

//import java.util.List;

public interface RecentSentimentExportsService extends RemoteService {
    //List<RecentSentimentExportsInfo>
    String getExports(long projectId); // throws ServiceException;
}
