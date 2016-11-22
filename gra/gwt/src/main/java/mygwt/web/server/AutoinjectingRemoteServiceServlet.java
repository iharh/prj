package mygwt.web.server;

import com.google.gwt.user.server.rpc.RPCRequest;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

public class AutoinjectingRemoteServiceServlet extends RemoteServiceServlet {
    //private static final Logger LOG = LoggerFactory.getLogger(AutoinjectingRemoteServiceServlet.class);

    private static final long serialVersionUID = 1L;

    private static WebApplicationContext ctx;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
        AutowireCapableBeanFactory beanFactory = ctx.getAutowireCapableBeanFactory();
        beanFactory.autowireBeanProperties(this, AutowireCapableBeanFactory.AUTOWIRE_BY_NAME, true);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // ... security stuff
        super.service(req, resp);
    }

    @Override
    protected void onAfterRequestDeserialized(RPCRequest rpcRequest) {
        // ... security stuff
        super.onAfterRequestDeserialized(rpcRequest);
    }
}
