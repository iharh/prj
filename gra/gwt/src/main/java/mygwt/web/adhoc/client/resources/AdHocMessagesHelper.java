package mygwt.web.adhoc.client.resources;

import mygwt.common.adhoc.AdHocConstants;
//import mygwt.web.adhoc.client.AdHocException;

import java.util.HashMap;
import java.util.Map;

public final class AdHocMessagesHelper {
    private static final Map<String, String> MAP = new HashMap<String, String>();
    /*private static final AdHocMessages MESSAGES = AdHocMessages.INSTANCE;

    static {
            MAP.put(AdHocConstants.UPLOAD_ERROR_CANTSAVE, MESSAGES.fileUploadErrorCantSave());
            
            MAP.put(AdHocConstants.SAMPLE_ERROR_HEADER, MESSAGES.fetchSampleDataMetadataError());
            MAP.put(AdHocConstants.SAMPLE_ERROR_EMPTY, MESSAGES.fetchSampleDataEmptyError());
            MAP.put(AdHocConstants.SAMPLE_ERROR_CANTREAD, MESSAGES.fetchSampleDataIOError());
            MAP.put(AdHocConstants.SAMPLE_ERROR_CANTOPEN, MESSAGES.fetchSampleDataCantOpenError());
            MAP.put(AdHocConstants.SAMPLE_ERROR_HEADER_DIGIT_COL_NAME, MESSAGES.fetchSampleDataDigitColNamesError());
            MAP.put(AdHocConstants.SAMPLE_ERROR_HEADER_CELL_EMPT, MESSAGES.fetchSampleDataHeaderCellEmptyError());
            //MAP.put(AdHocConstants.STORE_ERROR, MESSAGES.storeError());
            MAP.put(AdHocConstants.STORE_ERROR_CHECK_TYPE, MESSAGES.storeCheckTypeError());
            MAP.put(AdHocConstants.STORE_ERROR_CHECK_ATTR, MESSAGES.storeCheckAttributesError());
            MAP.put(AdHocConstants.STORE_ERROR_CHECK_RESERVED, MESSAGES.storeCheckAttributesReserved());
            MAP.put(AdHocConstants.RUN_ERROR, MESSAGES.runTmplError());
            MAP.put(AdHocConstants.SAMPLE_ERROR_HEADER_FIRST_ROW_EMPTY, MESSAGES.fetchSampleDataHeaderFirstRowEmpty());
            
    }
*/
    private AdHocMessagesHelper() {
    }
/*
    public static String getMessage(String key) {
        String result = "";
        if (key != null) {
            result = MAP.get(key.toLowerCase());
            if (result == null) result = "";
        }
        return result;
    }

    public static String getMessage(AdHocException e) {
        StringBuilder result = new StringBuilder();
        String key = e.getErrorKey();
        if (AdHocConstants.STORE_ERROR_CHECK_ATTR.equals(key) || AdHocConstants.STORE_ERROR_CHECK_RESERVED.equals(key)) {
            result.append(getMessage(key)).append("<br/>");
            result.append(composeNamesArray(e.getMessageArguments(), true));
            result.append("<br/>");
            result.append(MESSAGES.howToFixColumnTypes());
        } else if (AdHocConstants.STORE_ERROR_CHECK_TYPE.equals(key)) {
            result.append(getMessage(key)).append("<br/>");
            result.append(composeNamesArray(e.getMessageArguments(), true));			
        } else {
            result.append(getMessage(key));
        }
        return result.toString();
    }
*/
    public static String extractFileUploadError(String fileUploadResult) {
        String strippedError = null;
        int begin = fileUploadResult.indexOf(AdHocConstants.UPLOAD_RESULT_S);
        if (begin != -1) {
            begin += AdHocConstants.UPLOAD_RESULT_S.length();
            int end = fileUploadResult.indexOf(AdHocConstants.UPLOAD_RESULT_E, begin);
            if (end != -1) {
                strippedError = fileUploadResult.substring(begin, end);
            }
        }
        return strippedError;
    }
/*
    public static String composeNamesArray(Object[] objects, boolean withBR) {
        if (objects == null) {
            return "";
        }
        StringBuilder tmp = new StringBuilder();
        for (int i = 0; i < objects.length; i++) {
            String object = objects[i].toString();
            tmp.append(object);
            if (i < objects.length - 1) {
                tmp.append(", ");
                if (withBR) {
                    tmp.append("<br/>");
                }
            } else {
                tmp.append(".");
            }
        }		
        return tmp.toString();
    }

    public static String translateMetadataErrorMessage(String errorCode) {
        String result = "";
        String[] params = errorCode.split(AdHocConstants.PARAM_DELIM);
        if (params.length > 0) {
            result += AdHocMessages.INSTANCE.columnNum() + params[0]; // Column name
            if (params.length > 1) {
                if (AdHocConstants.SAMPLE_ERROR_HEADER_DUPL_COL_NAME.equals(params[1])) {
                    if (params.length > 2) {
                        result += ", " + MESSAGES.fetchSampleDataDuplColNamesError(params[2]);
                    }
                } else {
                    result += ", " + getMessage(params[1]); //$NON-NLS-1$
                }
            }
        }
        return result;
    }
*/
}
