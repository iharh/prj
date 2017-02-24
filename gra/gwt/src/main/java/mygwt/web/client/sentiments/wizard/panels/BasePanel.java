package mygwt.web.client.sentiments.wizard.panels;

import mygwt.web.client.sentiments.wizard.WizardPage;
import mygwt.web.client.sentiments.upload.SentimentUploadException;
import mygwt.web.client.sentiments.upload.resources.SentimentUploadMessagesHelper;

import mygwt.foundation.client.csrf.ProjectIdAware;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;

public class BasePanel extends VerticalPanel implements WizardPage {
    protected static final int borderW = 0;
    protected static final int spacing = 10; // 12

    private ProjectIdAware projectIdAware; 

    public BasePanel(ProjectIdAware projectIdAware) {
        super();
        this.projectIdAware = projectIdAware;

        setSize("100%", "auto");
        setSpacing(spacing);
        setBorderWidth(borderW);
    }

    public long getProjectId() {
        return projectIdAware.getProjectId();
    }

    protected static String handleException(SentimentUploadException caught) {
        String result = "<br/>";
        SentimentUploadException e = (SentimentUploadException) caught;
        String mess = SentimentUploadMessagesHelper.getMessage(e);
        result += (mess != null && !mess.isEmpty()) ? mess : e.getLocalizedMessage();
        return result;
    }

    @Override
    public void onEnter() {
    }
    @Override
    public boolean onLeave() {
        return true;
    }
    @Override
    public void onFinish() {
    }
}
