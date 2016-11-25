package com.clarabridge.web.sharedlexicon.wizard.server;

import mygwt.client.sentiments.RecentSentimentExportsInfo;
import mygwt.client.sentiments.service.RecentSentimentExportsService;

import mygwt.foundation.client.exception.ServiceException;

import java.util.Arrays;
import java.util.List;
//import java.io.IOException;

import org.apache.log4j.Logger;

public class RecentSentimentExportsServiceAction implements RecentSentimentExportsService {
    private static final Logger LOG = Logger.getLogger(RecentSentimentExportsServiceAction.class);
    private static final long serialVersionUID = 1L;

    @Override
    public List<RecentSentimentExportsInfo> getExports(long projectId) throws ServiceException {
        List<RecentSentimentExportsInfo> result = null;
        //try {
            result = Arrays.asList(
                new RecentSentimentExportsInfo("export1", "descr1", "2016/05/19", "p1", "f1"),
                new RecentSentimentExportsInfo("export2", "descr1", "2016/05/19", "p2", "f2")
            );
        //} catch (IOException e) {
        //    IOUtils.closeQuietly(input);
        //    LOG.error(e);
        //    throw new ServiceException(e.getMessage());
        //} 
        return result;
    }
}	
