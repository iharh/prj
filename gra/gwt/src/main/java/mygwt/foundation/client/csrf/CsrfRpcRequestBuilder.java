package mygwt.foundation.client.csrf;

import mygwt.foundation.shared.model.StringUtilHelper;

import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;

import java.util.HashSet;
import java.util.Set;

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
		rb.setHeader("X-Requested-With", this.getClass().getName());
		String tokenName = CsrfUtils.getTokenName();
		String tokenValue = CsrfUtils.getTokenValue();
			
		if (!StringUtilHelper.isNullOrEmpty(tokenName) 
				&& !StringUtilHelper.isNullOrEmpty(tokenValue)) { // IE fix.
			rb.setHeader(tokenName, tokenValue);
		}
		
		if (projectIdProvider != null) {
		//	rb.setHeader("X-ProjectId", String.valueOf(projectIdProvider.getProjectId()));
		}
		
		super.doFinish(rb);
	}

	@Override
	protected void doSetRequestData(RequestBuilder rb, String data) {
		super.doSetRequestData(rb, data);
		
		if (realSessionIgnoreMethods != null) {
			String methodName = extractMethodName(data);
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
