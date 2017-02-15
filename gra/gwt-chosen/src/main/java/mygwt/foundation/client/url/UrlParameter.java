package mygwt.foundation.client.url;

import com.google.gwt.user.client.Window;

public enum UrlParameter {
    PROJECT_ID, PROJECT_NAME, CLASSFN_ID, SENT_ID, VERB_ID, DOC_ID, NODE_ID, MODULE, FILTER_ID, SEARCH, TOKEN_ID, EXCEPION_ID;

    private String value = null;

    public void setValue(String value) {
	this.value = value;
    }

    private static String convertToCamelCase(String cn) {
        String result = "";
        String[] tokens = cn.split("_");
        boolean firstTime = true;
        for (String token : tokens) {
            if (firstTime) {
                result += token.toLowerCase();
                firstTime = false;
            } else {
                result += Character.toUpperCase(token.charAt(0));
                result += token.substring(1).toLowerCase();
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return convertToCamelCase(super.toString());
    }

    public long getValue() throws NumberFormatException {
        return Long.parseLong(getStringValue());
    }
    
    public Long getLongValue() throws NumberFormatException {
    	Long result = null;
    	String valueStr = getStringValue();
    	if (valueStr != null) {
    		result = Long.parseLong(valueStr);
    	}
    	
        return result; 
    }
    
    public boolean getBooleanValue() throws NumberFormatException {
    	return Boolean.valueOf(getStringValue());        
    }

    public String getStringValue() {
        if (null == value) {
            value = Window.Location.getParameter(this.toString());
        }
        return value;
    }
}
