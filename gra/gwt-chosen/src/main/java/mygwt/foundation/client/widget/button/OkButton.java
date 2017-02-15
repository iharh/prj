package mygwt.foundation.client.widget.button;

import com.google.gwt.event.dom.client.ClickHandler;

public class OkButton extends SimpleButton {

    public OkButton() {
        this("OK");
    }
    
    public OkButton(String text) {
        super(text);
        setStylePrimaryName("okButton");
    }
    
    public OkButton(String text, ClickHandler handler) {
        super(text, handler);
        setStylePrimaryName("okButton");
    }
}
