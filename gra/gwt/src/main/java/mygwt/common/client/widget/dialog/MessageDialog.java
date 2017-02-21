package mygwt.common.client.widget.dialog;

import mygwt.common.client.widget.resources.CommonClientWidgetMessages;

import mygwt.foundation.client.resources.CommonConstants;
import mygwt.foundation.client.widget.dialog.AlertDialog;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;

public final class MessageDialog extends AlertDialog {
    private MessageDialog(String caption, String message) {
        super(caption, message);
    }
    
    private MessageDialog(String caption, String message, HorizontalAlignmentConstant align) {
        super(caption, message, 400, 100, true, true, align);
    }

    private MessageDialog(String caption, String message, int width, int height) {
        super(caption, message, width, height);
    }
    
    private static void showDialog(String caption, String message, boolean text) {
        // TODO: AlertDialog should have default constructor
        MessageDialog dialog = new MessageDialog(caption, message);
        dialog.setShowAsText(text);
        dialog.show(false);
    }
    
    public static void show(String htmlMess) {
        showDialog("", htmlMess, false);
    }
    
    /**
     * Show error message.
     * @param error GTML based message.
     */
    public static void showError(String error) {
        showDialog(CommonConstants.INSTANCE.error(), error, false);
    }
    
    /**
     * Show message.
     * @param error GTML based message.
     */
    public static void showMessage(String message) {
        showDialog(CommonClientWidgetMessages.INSTANCE.dlgMessage(), message, false);
    }
    
    public static void showTextMessage(String message) {
        showDialog(CommonClientWidgetMessages.INSTANCE.dlgMessage(), message, true);
    }
    
    public static void showDialog(String caption, String message, ClickHandler okHandler) {
        MessageDialog dialog = new MessageDialog(caption, message);
        dialog.addOkHandler(okHandler);
        dialog.show(true);
    }
    
    public static void showDialog(String caption, String message, HorizontalAlignmentConstant align, ClickHandler okHandler) {
        MessageDialog dialog = new MessageDialog(caption, message,align);
        dialog.addOkHandler(okHandler);
        dialog.show(true);
    }

    public static void showDialog(String caption, String message, HorizontalAlignmentConstant align, ClickHandler okHandler, ClickHandler cancelHandler) {
        MessageDialog dialog = new MessageDialog(caption, message,align);
        dialog.addOkHandler(okHandler);
        dialog.addCancelListener(cancelHandler);
        dialog.show(true);
    }
    
    public static void showDialog(String caption, String message, int width, int height, ClickHandler okHandler) {
        MessageDialog dialog = new MessageDialog(caption, message, width, height);
        dialog.addOkHandler(okHandler);		
        dialog.show(true);
    }
    
    public static void showDialog(String caption, String message, int width, int height, ClickHandler okHandler, ClickHandler cancelHandler) {
        MessageDialog dialog = new MessageDialog(caption, message, width, height);
        dialog.addOkHandler(okHandler);
        dialog.addCancelListener(cancelHandler);		
        dialog.show(true);
    }
}
