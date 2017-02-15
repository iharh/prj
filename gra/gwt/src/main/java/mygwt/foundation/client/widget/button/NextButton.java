package mygwt.foundation.client.widget.button;

import mygwt.foundation.client.resources.CommonConstants;

public class NextButton extends SimpleButton {
    public NextButton() {
        this(CommonConstants.INSTANCE.next());
    }
    
    public NextButton(String text) {
        super(text);
        setStylePrimaryName("nextButton"); 
    }
}
