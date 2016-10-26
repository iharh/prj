package mygwt.foundation.client.widget.dialog;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class PopupShowNotificationEvent extends GwtEvent<PopupShowNotificationHanlder> {
    
    public static final Type<PopupShowNotificationHanlder> TYPE = new Type<PopupShowNotificationHanlder>();
    
    private final boolean displayPopup;
    
    public static void fire(boolean displayPopup, HasHandlers handlers) {
        PopupShowNotificationEvent event = new PopupShowNotificationEvent(displayPopup);
        handlers.fireEvent(event);
    }
    
    protected PopupShowNotificationEvent(boolean displayPopup) {
        this.displayPopup = displayPopup;
    }

    @Override
    public Type<PopupShowNotificationHanlder> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PopupShowNotificationHanlder handler) {
        handler.onPopupShow(this);
    }
    
    public boolean getDispalyPopup() {
        return displayPopup;
    }
}
