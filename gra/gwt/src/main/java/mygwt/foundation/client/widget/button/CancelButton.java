package mygwt.foundation.client.widget.button;

import com.google.gwt.event.dom.client.ClickHandler;

public class CancelButton extends SimpleButton {
    public CancelButton() {
        this("Cancel");
    }
    
    public CancelButton(String text) {
        super(text);
        setStylePrimaryName("cancelButton");
    }
    
    public CancelButton(String text, ClickHandler handler) {
        super(text, handler);
        setStylePrimaryName("cancelButton");
    }
}
