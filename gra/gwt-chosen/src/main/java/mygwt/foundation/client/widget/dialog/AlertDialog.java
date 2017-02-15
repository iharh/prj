package mygwt.foundation.client.widget.dialog;

import mygwt.foundation.client.widget.AjaxLoaderImage;
import mygwt.foundation.client.widget.button.CancelButton;
import mygwt.foundation.client.widget.button.OkButton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class AlertDialog extends BaseDialogBox implements IDialog {
    private OkButton ok;
    private CancelButton cancel;

    private boolean focused = true;
    private String message;
    private ClickHandler closeListener;
    private HTML messageHtml;
    private boolean showAsText;
    private Image busyIndicator;
        
    /**
     * Do not use this dialog directly to display simple massage. 
     * Use MessageDialog instead of this one.
     * 
     * @param caption
     * @param message HTML based text
     */
    public AlertDialog(String caption, String message) {
        this(caption, message, 250, 100);
    }
    
    public AlertDialog(String caption, String message, boolean closeOnOK) {
        this(caption, message, 250, 100, closeOnOK, true);
    }
    
    public AlertDialog(String caption, String message, boolean closeOnOK, boolean showCancel) {
        this(caption, message, 250, 100, closeOnOK, showCancel);
    }
    
    public AlertDialog(String caption, String message, int width, int height) {
    	this(caption, message, width, height, true, true);
    }
    
    public AlertDialog(String caption, String message, int width, int height, boolean closeOnOK, boolean showCancel) {
        super(caption, width, height);
        setWidget(createDialogContents(closeOnOK, showCancel));
        this.message = message;
    }
    
    public AlertDialog(String caption, String message, int width, int height, boolean closeOnOK, boolean showCancel,
            HorizontalAlignmentConstant align) {
        super(caption, width, height);
        setWidget(createDialogContents(closeOnOK, showCancel, align));
        this.message = message;
    }
    
    public void setOkCaption(String value) {
        ok.setText(value);
    }
    
    public void setCancelCaption(String value) {
        cancel.setText(value);
    }
    
    protected Widget createDialogContents(boolean closeOnOK, boolean showCancel) {
        return createDialogContents(closeOnOK, showCancel, HasHorizontalAlignment.ALIGN_CENTER);
    }
    
    protected Widget createDialogContents(boolean closeOnOK, boolean showCancel, HorizontalAlignmentConstant align) {
        VerticalPanel mainPanel = new VerticalPanel();
        HorizontalPanel buttonPanel = new HorizontalPanel();
        messageHtml = new HTML();
        ok = new OkButton();
        ok.setWidth("90px");
        ok.addStyleName("nowrap");
        cancel = new CancelButton();
        cancel.setWidth("80px");
        busyIndicator = new AjaxLoaderImage();
        busyIndicator.setVisible(false);
        
        closeListener = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                closeDialog(true);
            }
        };
        
        if (closeOnOK) {
            ok.addClickHandler(closeListener);
        }
        cancel.addClickHandler(closeListener);
        buttonPanel.add(ok);
        if (showCancel) {
            buttonPanel.add(cancel);
        }
        buttonPanel.add(busyIndicator);
        mainPanel.setStylePrimaryName("alertDialog"); //$NON-NLS-1$
        mainPanel.setHorizontalAlignment(align);
        mainPanel.add(messageHtml);
        mainPanel.add(buttonPanel);
        buttonPanel.setSpacing(5);
        mainPanel.setSpacing(5);
        return mainPanel;
    }
	
    public void closeDialog() {
        // Default
        closeDialog(false);
    }
    
    public void closeDialog(boolean autoClosed) {
        busyIndicator.setVisible(false);
        ok.setEnabled(true);
        cancel.setEnabled(true);
        AlertDialog.this.hide(autoClosed);
    }
    
    public void setBusyIndicator(boolean visible) {
    	busyIndicator.setVisible(visible);
    	if (busyIndicator.isVisible()) {
            ok.setEnabled(false);
            cancel.setEnabled(false);
    	}
    }
	
    public void show(boolean withCancel) {
        if (messageHtml != null) {
            if (showAsText) {
                messageHtml.setText(message);
            } else {
                messageHtml.setHTML(message);
            }
        }
        super.show();
        
        if (!withCancel) {
            cancel.setVisible(false);
        }
        /*
        if (ok != null) {
            ok.setFocus(focused);
        }
        */
    }
	
    public void show() {
        show(true);
    }
	
    public void addOkHandler(ClickHandler listener) {
        ok.addClickHandler(listener);
    }
    public void addCancelListener(ClickHandler listener) {
        cancel.addClickHandler(listener);
        super.addCloseButtonHandler(listener);
    }

    public void addCancelOnlyListener(ClickHandler listener) {
        cancel.addClickHandler(listener);
    }	

    public boolean isShowAsText() {
        return showAsText;
    }

    /**
     * Message should displayed as text, not as HTML.
     * @param showAsText
     */
    public void setShowAsText(boolean showAsText) {
        this.showAsText = showAsText;
    }
	
    @Deprecated
    /**
     * Method was added as a workaround, please set message via he constructors.
     */
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public void setOkHandler(ClickHandler listener) {
        addOkHandler(listener);
    }

    @Override
    public void setCancelListener(ClickHandler listener) {
        addCancelListener(listener);
    }

    @Override
    public void busy(boolean busy) {
        setBusyIndicator(busy);
    }
    
    protected OkButton getOkButton() {
        return ok;
    }
}
