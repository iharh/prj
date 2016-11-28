package mygwt.foundation.client.csrf;

import mygwt.foundation.shared.model.StringUtilHelper;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;

import java.util.HashSet;
import java.util.Set;

// http://www.gwtproject.org/javadoc/latest/com/google/gwt/user/client/rpc/RpcRequestBuilder.html
// http://www.programcreek.com/java-api-examples/index.php?api=com.google.gwt.user.client.rpc.RpcRequestBuilder

// http://stackoverflow.com/questions/6135590/gwt-rpc-data-format
// https://docs.google.com/document/d/1eG0YocsYYbNAtivkLtcaiEE5IOF5u4LUol8-LL0TIKU/edit

// http://blog.daniel-kurka.de/2016/07/gwt-rpcs-future.html
// https://dzone.com/articles/making-gwt-remote-procedure-ca

import com.google.gwt.user.client.Window;

public final class CsrfRpcRequestBuilder extends RpcRequestBuilder {
	
	private ProjectIdAware projectIdProvider;
	private Set<String> realSessionIgnoreMethods;

//	private static CsrfRpcRequestBuilder instance;
	
	private CsrfRpcRequestBuilder(ProjectIdAware projectIdProvider) {
		//super();
		this.projectIdProvider = projectIdProvider;
	}
	
	public static RpcRequestBuilder getInstance(ProjectIdAware projectIdProvider) {
//		if (instance == null) {
//			instance = new CsrfRpcRequestBuilder();
//		}
//		return instance;
		return new CsrfRpcRequestBuilder(projectIdProvider); // Currently, new instance for each service.
	}
	
    @Override
    protected void doFinish(RequestBuilder rb) {
        /*rb.setHeader("X-Requested-With", this.getClass().getName());
        String tokenName = CsrfUtils.getTokenName();
        String tokenValue = CsrfUtils.getTokenValue();
                
        if (!StringUtilHelper.isNullOrEmpty(tokenName) 
                        && !StringUtilHelper.isNullOrEmpty(tokenValue)) { // IE fix.
                rb.setHeader(tokenName, tokenValue);
        }
        
        if (projectIdProvider != null) {
        //	rb.setHeader("X-ProjectId", String.valueOf(projectIdProvider.getProjectId()));
        }*/
        
        Window.alert("doFinish start");
        super.doFinish(rb);
        Window.alert("doFinish done");
    }

	@Override
	protected void doSetRequestData(RequestBuilder rb, String data) {
		super.doSetRequestData(rb, data);
		
		final String methodName = extractMethodName(data);
                Window.alert("calling: " + methodName);

		if (realSessionIgnoreMethods != null) {
			if (methodName != null && realSessionIgnoreMethods.contains(methodName)) {
				rb.setHeader("X-RealSessionIgnore", methodName);
			}
		}
	}
	
	private String extractMethodName(String data) {
		String[] result = data.split("\\|"); 
		return result[6]; // Trick, can depend on gwt version.
	}

	public void addRealSessionIgnoreMethod(String methodName) {
		if (realSessionIgnoreMethods == null) {
			realSessionIgnoreMethods = new HashSet<String>();
		}
		realSessionIgnoreMethods.add(methodName);
	}
}
