package mygwt.foundation.client.url;

import com.google.gwt.core.client.GWT;

public final class Navigation {
    public static final String SERVICE_URL_SUFFIX = "_service";
    public static final String LOCALHOST = "http://localhost:8080/gwt/"; // /cmp/

    public static String getAbsoluteUrl(final Url url) {
        if (GWT.isScript()) {
            final String path = GWT.getHostPageBaseURL() + url.getRelativeUrl();
            return path;
        } else {
            return LOCALHOST + url.getRelativeUrl();
        }
    }

    private Navigation() {
    }
}
