package mygwt.web.client.sentiments.wizard.panels;

import com.google.gwt.user.client.ui.Label;

public class TempPanel extends BasePanel {
    private static String BUTTON_GROUP = "OPERATION_SELECTION_GROUP";

    public TempPanel(String name) {
        super();
        verticalPanel.add(new Label(name));
    }
}
