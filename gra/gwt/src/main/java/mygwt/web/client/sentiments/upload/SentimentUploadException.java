package mygwt.web.client.sentiments.upload;

import mygwt.foundation.client.exception.ServiceException;

public class SentimentUploadException extends ServiceException {
    private static final long serialVersionUID = 329928038599760625L;	

    private String errorKey;
    private String [] messageArguments;
    
    public SentimentUploadException() {
        super();
    }

    public SentimentUploadException(String message) {
        this(message, null);
    }
    
    public SentimentUploadException(String message, String errorKey) {
        super(message);
        this.errorKey = errorKey;
    }	
    
    public SentimentUploadException(String message, String errorKey, String [] messageArguments) {
        super(message);
        this.errorKey = errorKey;
        this.messageArguments = messageArguments;
    }	

    public String getErrorKey() {
        return errorKey;
    }

    public Object [] getMessageArguments() {
        return messageArguments;
    }

    public void setMessageArguments(String [] messageArguments) {
        this.messageArguments = messageArguments;
    }
}
