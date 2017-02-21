package mygwt.web.client.sentiments.upload.resources;

import mygwt.web.client.sentiments.upload.SentimentUploadException;

import mygwt.portal.dto.SentimentUploadConstants;

import java.util.HashMap;
import java.util.Map;

public final class SentimentUploadMessagesHelper {
    private static final Map<String, String> MAP = new HashMap<String, String>();
    
    static {
        MAP.put(SentimentUploadConstants.UPLOAD_ERROR_CANTSAVE, SentimentUploadMessages.INSTANCE.fileUploadErrorCantSave());
    }
    
    private SentimentUploadMessagesHelper() {
    }

    public static String getMessage(SentimentUploadException e) {
        StringBuilder result = new StringBuilder();
        String key = e.getErrorKey();

        result.append(getMessage(key));
        return result.toString();
    }

    public static String getMessage(String key) {
        String result = null;
        if (key != null) {
            result = MAP.get(key.toLowerCase());
        }
        return (result == null) ? "" : result;
    }
}
