package mygwt.foundation.client.widget.dialog;

import com.google.gwt.event.dom.client.ClickHandler;

public interface IDialog {
    void setOkHandler(ClickHandler listener);
    void setCancelListener(ClickHandler listener);
    void show();
    void hide();
    void busy(boolean busy);
}
