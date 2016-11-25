package mygwt.client.sentiments.service;

import mygwt.client.sentiments.RecentSentimentExportsInfo;

import mygwt.foundation.client.exception.ServiceException;

import com.google.gwt.user.client.rpc.RemoteService;

import java.util.List;

public interface RecentSentimentExportsService extends RemoteService {
    List<RecentSentimentExportsInfo> getExports(long projectId) throws ServiceException;
}
