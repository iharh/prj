package mygwt.foundation.client.util;

import com.google.gwt.user.client.Window.Navigator;

public final class ClientUtils {
    private ClientUtils() {
    }
    
    public static boolean isIE() {
        return Navigator.getUserAgent().toLowerCase().contains("msie")||  Navigator.getUserAgent().toLowerCase().contains("trident");
    }
    
    public static boolean isIE9() {
        return Navigator.getUserAgent().toLowerCase().contains("msie 9.0");
    }

    public static boolean isIE10() {
        String browserName = Navigator.getUserAgent().toLowerCase();
        return browserName.contains("msie 10") || (browserName.contains("msie 9") && browserName.contains("slcc2"));
    }
    
    public static boolean isIE8() {
        return Navigator.getUserAgent().toLowerCase().contains("msie 8.0");
    }
    
    public static boolean isSafari() {
        String ua = Navigator.getUserAgent().toLowerCase(); 
        return ua.contains("safari") && !ua.contains("chrome");
    }

    public static boolean isChrome() {
        return Navigator.getUserAgent().toLowerCase().contains("chrome");
    }
    
    public static boolean isIE7() {
    	return Navigator.getUserAgent().toLowerCase().contains("msie 7.0");
    }

    public static boolean isFirefox() {
        return Navigator.getUserAgent().toLowerCase().contains("firefox");
    }
}
