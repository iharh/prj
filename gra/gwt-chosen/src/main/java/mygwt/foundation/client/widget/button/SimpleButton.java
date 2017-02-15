package mygwt.foundation.client.widget.button;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.WhiteSpace;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Focusable;
import com.google.gwt.user.client.ui.HasEnabled;
import com.google.gwt.user.client.ui.Label;

/**
 * Square-styled button emulation. Dark style by default.
 * CSS classes are used:
 *    .simpleButton - default button style
 *    .simpleButton-disabled - disabled style
 *    .simpleButton:before/after - to add icons (see css for .okButton:before)
 * See also OkButton, CancelButton
 * 
 * Note: className should contain "button" word if you want it to be tagged properly (for autotesting)
 * @author Yurii_Malchevskyi
 *
 */
public class SimpleButton extends Label implements HasEnabled, Focusable {

    private boolean isEnabled = true;
    
    /**
     * Constructor for icon button
     */
    public SimpleButton() {
        setStylePrimaryName("iconButton"); //$NON-NLS-1$
    }
    

    /**
     * Constructor for square button with text
     */
    public SimpleButton(String text) {
        super(text);
        setStylePrimaryName("simpleButton"); //$NON-NLS-1$
    }
    
    public SimpleButton(String text, ClickHandler handler) {
        super(text);
        setStylePrimaryName("simpleButton"); //$NON-NLS-1$
        addClickHandler(handler);
    }
    
    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    @Override
    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
        if (enabled) {
            sinkEvents(Event.ONCLICK);
            removeStyleDependentName("disabled"); //$NON-NLS-1$
            refreshCSS();
        } else {
            unsinkEvents(Event.ONCLICK);
            addStyleDependentName("disabled"); //$NON-NLS-1$
            refreshCSS();
        }
    }
    
    /**
     * This method is required, because styles are not updated in some cases (on popups in chrome)
     * for unknown reason.
     * WhiteSpace is used to avoid interference with other styles
     */
    private void refreshCSS() {
        getElement().getStyle().setWhiteSpace(WhiteSpace.NORMAL);
        getElement().getStyle().clearWhiteSpace();
    }

    @Override
    public int getTabIndex() {
        return getElement().getTabIndex();
    }

    @Override
    public void setAccessKey(char key) {
        // nothing
    }

    @Override
    public void setFocus(boolean focused) {
        if (focused)
            getElement().focus();
        else
            getElement().blur();
    }

    @Override
    public void setTabIndex(int index) {
        getElement().setTabIndex(index);
    }
    
    public void click() {
        DomEvent.fireNativeEvent(Document.get().createClickEvent(0, 0, 0, 0, 0,
                false, false, false, false), this);
    }
}
