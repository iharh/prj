package mygwt.web.client.sentiments.wizard.panels;

import mygwt.web.client.sentiments.wizard.WizardPage;

import mygwt.foundation.client.csrf.ProjectIdAware;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

public class BasePanel extends VerticalPanel implements WizardPage, ProjectIdAware {
    protected static final int borderW = 0;
    protected static final int spacing = 10; // 12

    private long projectId;

    public BasePanel() {
        super();
        setSize("100%", "auto");
        setSpacing(spacing);
        setBorderWidth(borderW);
    }

    @Override
    public void onEnter() { }
    @Override
    public void onLeave() { }
    @Override
    public void onFinish() { }

    // ApplicationContext.get().getProjectId();

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    @Override
    public long getProjectId() {
        return projectId;
    }
}
