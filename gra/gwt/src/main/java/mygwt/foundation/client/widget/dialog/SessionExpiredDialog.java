package mygwt.foundation.client.widget.dialog;

import mygwt.foundation.client.csrf.CsrfFormPanel;
import mygwt.foundation.client.resources.CommonClientMessages;
import mygwt.foundation.client.resources.CommonConstants;
import mygwt.foundation.client.widget.button.OkButton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public final class SessionExpiredDialog extends BaseDialogBox {
    private static final SessionExpiredDialog INSTANCE = new SessionExpiredDialog();
    
    public static void showDialog() {
        if (!INSTANCE.isShowing()) {
            INSTANCE.show();
        }
    }
    
    private SessionExpiredDialog() {
        super(CommonClientMessages.INSTANCE.dlgWarning(), 240, 100);
        setWidget(createDialogContents());
    }

    private Widget createDialogContents() {
        VerticalPanel mainPanel = new VerticalPanel();
        HorizontalPanel buttonPanel = new HorizontalPanel();
        HTML messageHtml = new HTML(CommonClientMessages.INSTANCE.dlgMsgSessionExpired());
        OkButton ok = new OkButton(CommonConstants.INSTANCE.ok());
        ok.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        }); 
        ok.setWidth("80px");
        buttonPanel.add(ok);
        mainPanel.setStylePrimaryName("alertDialog");
        mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        mainPanel.add(messageHtml);
        mainPanel.add(buttonPanel);
        buttonPanel.setSpacing(5);
        mainPanel.setSpacing(5);
        return mainPanel;
    }
    
    @Override
    protected void onClose() {
        CsrfFormPanel.reload(); // reload anyway to get to login page         
    }
}
