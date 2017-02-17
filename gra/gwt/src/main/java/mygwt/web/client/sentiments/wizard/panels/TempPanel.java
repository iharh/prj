package mygwt.web.client.sentiments.wizard.panels;

import com.google.gwt.user.client.ui.Label;

public class TempPanel extends BasePanel {
    public TempPanel(String name) {
        super();
        verticalPanel.add(new Label(name));
    }
}
