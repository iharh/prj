package mygwt.foundation.client.widget.dialog;

import mygwt.foundation.client.resources.CommonConstants;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

public class YesNoDialog extends AlertDialog {
    private ClickHandler closeHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            hide();
        }
    };
    
    public YesNoDialog(String caption, String message, ClickHandler yesHandler) {
        this(caption, message, yesHandler, null);
    }

    public YesNoDialog(String caption, String message, ClickHandler yesHandler, ClickHandler noHandler) {
        super(caption, message);
        setOkCaption(CommonConstants.INSTANCE.yes());
        setCancelCaption(CommonConstants.INSTANCE.no());
        addOkHandler(yesHandler != null ? yesHandler : closeHandler);
        addCancelListener(noHandler != null ? noHandler : closeHandler);
    }
    
    public YesNoDialog(String caption, String message, ClickHandler yesHandler, ClickHandler noHandler, ClickHandler closeHandler) {
        super(caption, message);
        setOkCaption(CommonConstants.INSTANCE.yes());
        setCancelCaption(CommonConstants.INSTANCE.no());
        addOkHandler(yesHandler != null ? yesHandler : closeHandler);
        addCancelOnlyListener(noHandler != null ? noHandler : closeHandler);
        addCloseButtonHandler(closeHandler);
    }   
}
