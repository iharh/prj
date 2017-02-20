package mygwt.web.client.sentiments.wizard.panels;

import mygwt.web.client.sentiments.wizard.WizardPage;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

public class BasePanel extends VerticalPanel implements WizardPage {
    private static final int borderW = 0;
    private static final int spacing = 10; // 12

    public BasePanel() {
        super();
        setSize("100%", "auto"); // % or absolute value lead to controls streach
        setSpacing(spacing);
        setBorderWidth(borderW);
    }

    @Override
    public void onEnter() { }
    @Override
    public void onLeave() { }
    @Override
    public void onFinish() { }
}
