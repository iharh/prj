package mygwt.common.exception;

//import mygwt.common.i18n.MessageHelper;

public class CMPException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String UNDEFINED_MSG_CODE = "";
    
    private final String messageCode;
    private final Object[] messageArguments;
/*
    public CMPException(String messageCode, Object[] messageArguments, Throwable cause) {
        super(MessageHelper.getMessage(messageCode, messageArguments), cause);
        this.messageCode = messageCode;
        this.messageArguments = messageArguments;
    }
	
    public CMPException(String messageCode, Object[] messageArguments) {
        super(MessageHelper.getMessage(messageCode, messageArguments));
        this.messageCode = messageCode;
        this.messageArguments = messageArguments;
    }
*/
    public CMPException() {
        super();
        messageCode = UNDEFINED_MSG_CODE;
        messageArguments = null;
    }

    public CMPException(String message) {
        super(message);
        messageCode = "";
        messageArguments = null;
    }

    public CMPException(Throwable cause) {
        super(cause);
        messageCode = UNDEFINED_MSG_CODE;
        messageArguments = null;
    }

    public CMPException(String message, Throwable cause) {
        super(message, cause);
        messageCode = UNDEFINED_MSG_CODE;
        messageArguments = null;
    }

    public Object[] getMessageArguments() {
        return messageArguments;
    }

    public String getMessageCode() {
        if ("".equals(messageCode))
            return this.getMessage();
        return messageCode;
    }
}

