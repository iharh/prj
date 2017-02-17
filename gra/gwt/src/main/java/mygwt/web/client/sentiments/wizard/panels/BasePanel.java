package mygwt.web.client.sentiments.wizard.panels;

import mygwt.web.client.sentiments.wizard.WizardPage;

import com.google.gwt.user.client.ui.DockPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

public class BasePanel extends DockPanel implements WizardPage {
    private static String BUTTON_GROUP = "OPERATION_SELECTION_GROUP";

    private static final int borderW = 0;
    private static final int spacing = 10; // 12

    protected VerticalPanel verticalPanel = new VerticalPanel();

    public BasePanel() {
        super();

        this.setBorderWidth(borderW);
        this.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        verticalPanel = new VerticalPanel();
        verticalPanel.setWidth("100%");
        verticalPanel.setSpacing(spacing);

	this.add(verticalPanel, DockPanel.NORTH);
    }

    @Override
    public void onEnter() { }
    @Override
    public void onLeave() { }
    @Override
    public void onFinish() { }
}
