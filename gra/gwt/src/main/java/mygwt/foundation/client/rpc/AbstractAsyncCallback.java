package mygwt.foundation.client.rpc;

//import mygwt.foundation.client.exception.ServiceException;
//import mygwt.foundation.client.exception.AccessDeniedClientException;
//import mygwt.foundation.client.exception.AuthenticationClientException;
//import mygwt.foundation.client.exception.ObjectNotFoundClientException;
//import mygwt.foundation.client.exception.LockedNodeException;

import mygwt.foundation.client.resources.CommonClientMessages;
import mygwt.foundation.client.resources.CommonConstants;

import mygwt.foundation.client.url.Action;

import mygwt.foundation.client.util.SimpleHtmlSanitizerEx;

import mygwt.foundation.client.widget.dialog.AlertDialog;
//import mygwt.foundation.client.widget.dialog.SessionExpiredDialog;

import mygwt.foundation.shared.model.StringUtilHelper;

import com.google.gwt.core.client.GWT;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.logging.client.SimpleRemoteLogHandler;

import com.google.gwt.user.client.rpc.AsyncCallback;
//import com.google.gwt.user.client.rpc.InvocationException;
//import com.google.gwt.user.client.rpc.StatusCodeException;
import com.google.gwt.user.client.ui.HasHTML;

import java.util.logging.Level;
import java.util.logging.LogRecord;

public abstract class AbstractAsyncCallback<T> implements AsyncCallback<T> {
    private static final String LOGIN_PAGE_PART = "<script ";

    public static final AbstractAsyncCallback<Void> VOID_CALLBACK = new AbstractAsyncCallback<Void>() {
        @Override
        public void onSuccess(Void arg0) {
        }
    };
    
    /*public static interface LockStateListener {
        void onLockStateChanged(long nodeId, int statte, long lockUserId);
        void lockCurrentItem(long nodeId, int statte, long lockUserId);
    }
    
    private static boolean showServerNotAvailableException = true;

    public static void setShowServerNotAvailableException(boolean showStatusCodeException) {
        AbstractAsyncCallback.showServerNotAvailableException = showStatusCodeException;
    }*/

    private HasHTML messageOutput;

    protected HasHTML getMessageOutput() {
        return messageOutput;
    }

    protected void setMessageOutput(HasHTML messageOutput) {
        this.messageOutput = messageOutput;
    }

    private class ErrorOkHandler implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            AbstractAsyncCallback.this.onErrorOkClick(event);
        }
    }

    protected void onErrorOkClick(ClickEvent event) {
    }

    /**
     * DO NOT OVERRIDE unless you want to override generic exception handling like authentication errors!
     * Override processOtherExceptions instead.
     */
    @Override
    public void onFailure(Throwable caught) {
        proccessException(caught);
    }
    
    // DO NOT OVERRIDE unless you want to override generic exception handling like authentication errors!
    // Override processOtherExceptions instead.
    public void onFailure(Throwable caught, String newMessage) {
        proccessException(caught, newMessage);
    }

    // Override to specify error message.
    protected String errorMessage() {
        return "Your Last Operation Failed"; // !!!CommonClientMessages.INSTANCE.yourLastOpFailed();
    }
    
    // Override to change behavior.
    protected void showErrorMessageAlert(String mess, ErrorOkHandler okHandler) {
        if (messageOutput == null) {
            AlertDialog alertDialog = new AlertDialog(CommonConstants.INSTANCE.error(), SimpleHtmlSanitizerEx.sanitizeHtml(mess).asString());
            alertDialog.addOkHandler(okHandler);
            alertDialog.show(false);
        } else {
            messageOutput.setHTML(mess);
        }
    }

    // Override to change behavior.
    protected void showErrorMessageAlert(String mess) {
    	showErrorMessageAlert(mess, new ErrorOkHandler());
    }

    public abstract void onSuccess(T result);
    
    private void proccessException(Throwable caught) {
    	proccessException(caught, errorMessage());
    }

    private void proccessException(Throwable caught, String newMessage) {
        String message = newMessage;
        if (null != message && !message.isEmpty()) {
            message = CommonClientMessages.INSTANCE.boldMessage(SimpleHtmlSanitizerEx.sanitizeHtml(message).asString());
        } else {
            message = CommonConstants.EMPTY_STRING;
        }

        GWT.log(message, caught);
        
        // Remote logging block:START
        try {
            // Try to log same exception remotely
            SimpleRemoteLogHandler remoteLog = new SimpleRemoteLogHandler();
            LogRecord lr = new LogRecord(Level.INFO, message + "\r\n Obfuscated trace: \r\n" +getMessage(caught) +"\r\n");//$NON-NLS-1$
            lr.setThrown(caught);
            lr.setLoggerName("com.google.gwt.logging.server"); //$NON-NLS-1$
            remoteLog.publish(lr);
        } catch (Throwable th){
            // nothing
        }
        // Remote logging block:END

        /*if (caught instanceof AccessDeniedClientException) {
            message += CommonClientMessages.INSTANCE.userDoesNotHave();
            showErrorMessageAlert(message);
        } else if (caught instanceof AuthenticationClientException) {
            handleAuthenticateError();
        } else if (caught instanceof ObjectNotFoundClientException) {
            handleObjectNotFoundError((ObjectNotFoundClientException) caught);
        } else if (caught instanceof LockedNodeException) {
            handleLockedNodeException((LockedNodeException) caught);
        } else if (caught instanceof StatusCodeException) {
            processStatusCodeException(message, (StatusCodeException) caught);
        } else if (caught instanceof InvocationException) {
            //if (isLoginPage(caught.getLocalizedMessage())) {
            //    handleAuthenticateError();
            //} else {
            	showErrorMessageAlert(message);
            //}
        } else if (caught instanceof ServiceException) {
            //if (isLoginPage(caught.getLocalizedMessage())) {
            //    handleAuthenticateError();
            //} else {
                String localizedMsg = caught.getLocalizedMessage();
                if (!StringUtilHelper.isNullOrEmpty(localizedMsg)) {
                    message += localizedMsg;
                }
                showErrorMessageAlert(message);
            //}
        } else {
            //if (isLoginPage(caught.getLocalizedMessage())) {
            //    handleAuthenticateError();
            //} else {
                processOtherExceptions(caught, message);
            //}
        }
        */
    }

    protected void processOtherExceptions(Throwable caught, String newMessage) {
        showErrorMessageAlert(
            CommonClientMessages.INSTANCE.errorMsgAlert(newMessage, caught.getClass().toString(),
                caught.getLocalizedMessage()).asString());
    }
/*
    protected void processStatusCodeException(String currentMessage, StatusCodeException sce) {
        if (sce.getStatusCode() == 403) {
            // StatusCodeException.getLocalizedMessage() contains HTML page.
            handleAuthenticateError();
        } else if (sce.getStatusCode() == 410) { // TODO: ADM: Use specialized Exception class instead!!!
            showErrorMessageAlert(sce.getEncodedResponse(), new ErrorOkHandler() {
				@Override
				public void onClick(ClickEvent event) {
					Action.PROJECTS.redirect();
				}
            }); 
        } else if (sce.getStatusCode() == 0) {
            GWT.log("Status code 0 received", sce); //$NON-NLS-1$
		} else if (sce.getStatusCode() < 100 || sce.getStatusCode() > 599) { //status code is not in supported range
            
            handleServerNotAvailbaleError();
        } else {
            String message = CommonClientMessages.INSTANCE.serverReturns(currentMessage, sce.getStatusCode());
            showErrorMessageAlert(message);
        }
    }
/*
    private void handleAuthenticateError() {
        if (messageOutput == null) {
            SessionExpiredDialog.showDialog();
        } else {
            String message = CommonClientMessages.INSTANCE.userIsNotAuthenticated();
            messageOutput.setHTML(message);
        }
    }
    
    private void handleServerNotAvailbaleError() {
        if (showServerNotAvailableException) {
            String message = CommonClientMessages.INSTANCE.serverUnavailable();

            if (messageOutput == null) {
                AlertDialog alertDialog = new AlertDialog(CommonConstants.INSTANCE.error(), message);
                alertDialog.addOkHandler(new ErrorOkHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        // super.onClick(event);
                        Action.LOGIN.redirect();
                    }
                });
                alertDialog.show(false);
            } else {
                messageOutput.setHTML(message);
            }
        }
    }

    private void handleObjectNotFoundError(ObjectNotFoundClientException clientException) {
        String message = generateMessage(clientException);
        showErrorMessageAlert(message);
        //if (messageOutput == null) {
        //  ObjectNotFoundDialog.showDialog(message, new ErrorOkHandler());
        //} else {
        //    messageOutput.setHTML(message);
        //}
    }
    
    private String generateMessage(ObjectNotFoundClientException clientException) {
        String name = clientException.getName() != null ?
            CommonClientMessages.INSTANCE.boldMessage(clientException.getName()) : CommonConstants.EMPTY_STRING;
        String message = CommonClientMessages.INSTANCE.nodeDoesNotExists(name);
        return message;
    }

    private void handleLockedNodeException(final LockedNodeException exception) {
        String message = CommonClientMessages.INSTANCE.nodeLocked();

        if (messageOutput == null) {
            
            AlertDialog alertDialog = new AlertDialog(CommonConstants.INSTANCE.error(), message);
            alertDialog.addOkHandler(new ErrorOkHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    super.onClick(event);
                    
                }
            });
            alertDialog.show(false);
        } else {
            messageOutput.setHTML(message);
        }
    }
*/
    private boolean isLoginPage(final String message) {
        return message != null && message.indexOf(LOGIN_PAGE_PART) >= 0;
    }
    
    private static String getMessage(Throwable throwable) {
        String ret="";
        while (throwable!=null) {
            if (throwable instanceof com.google.gwt.event.shared.UmbrellaException){
                for (Throwable thr2 :((com.google.gwt.event.shared.UmbrellaException)throwable).getCauses()){
                    if (ret != "")
                        ret += "\nCaused by: ";
                    ret += thr2.toString();
                    ret += "\n  at "+getMessage(thr2);
                }
            } else if (throwable instanceof com.google.web.bindery.event.shared.UmbrellaException){
                for (Throwable thr2 :((com.google.web.bindery.event.shared.UmbrellaException)throwable).getCauses()){
                    if (ret != "")
                        ret += "\nCaused by: ";
                    ret += thr2.toString();
                    ret += "\n  at "+getMessage(thr2);
                }
            } else {
                if (ret != "")
                    ret += "\nCaused by: ";
                ret += throwable.toString();
                for (StackTraceElement sTE : throwable.getStackTrace())
                    ret += "\n  at "+sTE;
            }
            throwable = throwable.getCause();
        }
        return ret;
    }
}
