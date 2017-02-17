package mygwt.web.client.utils;

public class LogUtils {
    public static native void log(String msg) /*-{
        $wnd.alert(msg);
    }-*/;
}
