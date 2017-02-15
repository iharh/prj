package mygwt.foundation.client.widget.button;

import mygwt.foundation.client.resources.CommonConstants;

public class BackButton extends SimpleButton {
    public BackButton() {
        this(CommonConstants.INSTANCE.back());
    }
    
    public BackButton(String text) {
        super(text);
        setStylePrimaryName("backButton");
    }
}
