package mygwt.web.server;

//import javax.servlet.http.HttpServletRequest;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public abstract class CmpRemoteServletSupport extends AutoinjectingRemoteServiceServlet {
    //private static final Logger LOG = LoggerFactory.getLogger(CmpRemoteServletSupport.class);
    
    private static final long serialVersionUID = 1L;
/*
    // Returns url like http://somehost/cmp, needed to support IIS-CMP forward support.
    @SuppressWarnings("nls")
    public String getInstanceUrl() {
        String scheme = getServletRequest().getScheme(); // http/https
        String server = getServletRequest().getServerName();
        int port = getServletRequest().getServerPort();
        String context = getServletRequest().getContextPath(); // /cmp

        StringBuilder buf = new StringBuilder(scheme);
        buf.append("://");
        buf.append(server);

        if ("http".equalsIgnoreCase(scheme) && port != 80 || "https".equalsIgnoreCase(scheme) && port != 443) { // non standard port ////$NON-NLS-1$
            buf.append(":");
            buf.append(port);
        }

        buf.append(context);
        buf.append("/");

        return buf.toString();
    }

    public HttpServletRequest getServletRequest() {
        return super.getThreadLocalRequest();
    }
*/
}
