package mygwt.foundation.client.url;

import mygwt.foundation.client.csrf.CsrfFormPanel;
import mygwt.foundation.client.csrf.CsrfFormPanel.Target;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FormPanel;

import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;

public enum Action implements Url {
    CATROLLUP, PROJECTS, SOURCE_HIGHLIGHTER, ACEGI_LOGIN, CLASSFN_WIZARD, CHANGE_PASSWORD, ANALYZE, LOGIN;

    @Override
    public String getRelativeUrl() {
    	return this.name().toLowerCase();
    }

    @Override
    public String getAbsoluteUrl() {
        return Navigation.getAbsoluteUrl(this);
    }

    @Override
    public String toString() {
        return getAbsoluteUrl();
    }

    /**
     * redirects user to a chosen action with specified parameters.
     * 
     * @param parameters - can be null if no parameters are needed for redirection
     */
    public void redirect(Map<UrlParameter, String> parameters) {
        String urlParameters = "";
        if (null != parameters) {
            boolean isFirst = true;
            for (Entry<UrlParameter, String> entry : parameters.entrySet()) {
                if (isFirst) {
                    urlParameters += "?";
                    isFirst = false;
                } else {
                    urlParameters += "&";
                }
                urlParameters += entry.getKey().toString() + "=" + entry.getValue();
            }
        }
        
        // CSRF
        FormPanel form = new CsrfFormPanel(Target.SELF);
	    form.setAction(getAbsoluteUrl() + urlParameters);
	    form.submit();
    }

    // FIXME: There is OOP should be used!!!
    public void redirect() {
        Map<UrlParameter, String> parameters = null;
        switch (this) {
            case CATROLLUP:
                parameters = new HashMap<UrlParameter, String>();
                parameters.put(UrlParameter.PROJECT_ID, UrlParameter.PROJECT_ID.getStringValue());
                parameters.put(UrlParameter.CLASSFN_ID, UrlParameter.CLASSFN_ID.getStringValue());        		 
                break;
            case ACEGI_LOGIN:
                // no parameters
                break;
            case PROJECTS:
                // no parameters
                break;
            case SOURCE_HIGHLIGHTER:
                parameters = new HashMap<UrlParameter, String>();
                parameters.put(UrlParameter.PROJECT_ID, UrlParameter.PROJECT_ID.getStringValue());
                parameters.put(UrlParameter.SENT_ID, UrlParameter.SENT_ID.getStringValue());
                break;
            case CLASSFN_WIZARD:
                parameters = new HashMap<UrlParameter, String>();
                parameters.put(UrlParameter.PROJECT_ID, UrlParameter.PROJECT_ID.getStringValue());
                break;
            default:
        }
        redirect(parameters);
    }

    @Override
    public String getAbsoluteUrlNoSuffix() {
        return GWT.getHostPageBaseURL() + this.name().toLowerCase();
    }
}
