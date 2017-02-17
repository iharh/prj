package mygwt.web.client.sentiments.rse.server;

import mygwt.portal.dto.sentiments.rse.RecentSentimentExportsInfo;

import mygwt.web.client.sentiments.rse.RecentSentimentExportsService;

import mygwt.web.server.CmpRemoteServletSupport;

import mygwt.foundation.client.exception.ServiceException;

//import org.apache.commons.io.IOUtils;

//import java.io.IOException;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

public class RecentSentimentExportsServiceImpl extends CmpRemoteServletSupport implements RecentSentimentExportsService {
    private static final Logger log = Logger.getLogger(RecentSentimentExportsServiceImpl.class);

    private static final long serialVersionUID = 1L;
	
    @Override
    public List<RecentSentimentExportsInfo> getExports(long projectId) /*throws ServiceException*/ {
        log.info("getExports called");
        List<RecentSentimentExportsInfo> result = null;
        //try {
            result = Arrays.asList(
                new RecentSentimentExportsInfo("export0", "descr0", "2016/05/19", false, false),
                new RecentSentimentExportsInfo("export1", "descr1", "2016/05/19", false, true),
                new RecentSentimentExportsInfo("export2", "descr2", "2016/05/19", true, false),
                new RecentSentimentExportsInfo("export3", "descr3", "2016/05/19", true, true),
                new RecentSentimentExportsInfo("export4", "descr4", "2016/05/19", false, false),
                new RecentSentimentExportsInfo("export5", "descr5", "2016/05/19", false, false),
                new RecentSentimentExportsInfo("export6", "descr6", "2016/05/19", false, false),
                new RecentSentimentExportsInfo("export7", "descr7", "2016/05/19", false, false),
                new RecentSentimentExportsInfo("export8", "descr8", "2016/05/19", false, false),
                new RecentSentimentExportsInfo("export9", "descr9", "2016/05/19", false, false)
            );
        //} catch (IOException e) {
        //    IOUtils.closeQuietly(input);
        //    LOG.error(e);
        //    throw new ServiceException(e.getMessage());
        //} 
        return result;
    }
}
