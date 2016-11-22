package mygwt.foundation.client.csrf;

/**
 * CSRF protection utility methods.
 */
public class CsrfUtils {
    public static native String getTokenName() /*-{
            return $wnd.properties['tokenName'];
    }-*/;

    public static native String getTokenValue() /*-{
            return $wnd.properties['tokenValue'];
    }-*/;
}
