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
                new RecentSentimentExportsInfo("export0", "descr0", "2016/05/19", "p0", "f0"),
                new RecentSentimentExportsInfo("export1", "descr1", "2016/05/19", "p1", "f1"),
                new RecentSentimentExportsInfo("export2", "descr2", "2016/05/19", "p2", "f2"),
                new RecentSentimentExportsInfo("export3", "descr3", "2016/05/19", "p3", "f3"),
                new RecentSentimentExportsInfo("export4", "descr4", "2016/05/19", "p4", "f4"),
                new RecentSentimentExportsInfo("export5", "descr5", "2016/05/19", "p5", "f5"),
                new RecentSentimentExportsInfo("export6", "descr6", "2016/05/19", "p6", "f6")
            );
        //} catch (IOException e) {
        //    IOUtils.closeQuietly(input);
        //    LOG.error(e);
        //    throw new ServiceException(e.getMessage());
        //} 
        return result;
    }
}
