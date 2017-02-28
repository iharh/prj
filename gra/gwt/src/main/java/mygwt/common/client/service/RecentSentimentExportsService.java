package mygwt.common.client.service;

import mygwt.common.security.annotation.SecureParameter;
import mygwt.common.security.model.ClassName;
import mygwt.common.security.model.PermissionName;
import mygwt.portal.dto.sentiments.rse.RecentSentimentExportsInfo;

// import mygwt.foundation.client.exception.ServiceException;

import com.google.gwt.user.client.rpc.RemoteService;

import java.util.List;

public interface RecentSentimentExportsService extends RemoteService {
    @SecureParameter(permission = PermissionName.READ, strategyClass = ClassName.SENTIMENTS)
    List<RecentSentimentExportsInfo> getExports(long projectId); // throws ServiceException;
}
