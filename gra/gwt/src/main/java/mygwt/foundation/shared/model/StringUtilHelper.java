package mygwt.foundation.shared.model;

public final class StringUtilHelper {
    /**
     * Browser specific. IE passes string "null" in field values for null argument while FF passes just empty string "".
     */
    public static boolean isNullOrEmpty(String string) {
        return string == null || "".equals(string) || "null".equalsIgnoreCase(string)
            || "undefined".equalsIgnoreCase(string);
    }
}
