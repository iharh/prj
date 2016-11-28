package mygwt.web.client.sentiments.server;

import mygwt.web.client.sentiments.RecentSentimentExportsInfo;
import mygwt.web.client.sentiments.RecentSentimentExportsService;

import mygwt.web.server.CmpRemoteServletSupport;

import mygwt.foundation.client.exception.ServiceException;

//import mygwt.common.exception.CMPException;

//import org.apache.commons.io.IOUtils;

// import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;

//import java.io.IOException;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

//@Controller
//@RequestMapping("/.../*")
public class RecentSentimentExportsServiceImpl extends CmpRemoteServletSupport implements RecentSentimentExportsService {
    private static final Logger log = Logger.getLogger(RecentSentimentExportsServiceImpl.class);

    private static final long serialVersionUID = 1L;
	
    @Override
    public List<RecentSentimentExportsInfo> getExports(long projectId) /*throws ServiceException*/ {
        log.info("getExports called");
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
