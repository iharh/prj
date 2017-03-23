package mygwt.web.client.sentiments.wizard.panels;

import mygwt.web.client.sentiments.wizard.FinishHandler;
import mygwt.web.client.sentiments.resources.SentimentsMessages;
import mygwt.web.client.report.ExportPanel;
import mygwt.web.client.utils.StyleUtils;
import mygwt.foundation.client.csrf.ProjectIdAware;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;

public class CurrentExportPanel extends BasePanel {
    private static final String textBoxW = "470px";
    private static final int labelW      = 150;
    private static final int spacing     = 12;

    private FinishHandler finishHandler;

    private SentimentsMessages msgs;

    private CheckBox exportWords;
    private CheckBox exportRules;
    private TextBox exportName;
    private TextArea exportDescription;

    public CurrentExportPanel(ProjectIdAware projectIdAware, FinishHandler finishHandler) {
        super(projectIdAware); // addStyleName("myExportPanel");
        this.finishHandler = finishHandler;

        msgs = SentimentsMessages.INSTANCE;

        add(new InlineLabel(msgs.exportLabelTop()));
        add(createNamePanel());
        add(createDescriptionPanel());
        add(createContentPanel());
    }

    private Panel createNamePanel() {
        HorizontalPanel namePanel = new HorizontalPanel();
        namePanel.setBorderWidth(borderW);
        namePanel.setSpacing(spacing);

        InlineLabel labelName = new InlineLabel(msgs.exportName());
        namePanel.add(labelName);
        exportName = new TextBox();
        exportName.setWidth(textBoxW);
        namePanel.add(exportName);
        alignLabelW(labelName, labelW);

        return namePanel;
    }

    private Panel createDescriptionPanel() {
        HorizontalPanel descriptionPanel = new HorizontalPanel();
        descriptionPanel.setBorderWidth(borderW);
        descriptionPanel.setSpacing(spacing);

        InlineLabel labelDescription = new InlineLabel(msgs.exportDescription());
        descriptionPanel.add(labelDescription);
        exportDescription = new TextArea();
        exportDescription.setCharacterWidth(80);
        exportDescription.setVisibleLines(3);
        exportDescription.setWidth(textBoxW);
        exportDescription.setHeight("80px");
        exportDescription.addStyleName("descr-TextArea");
        descriptionPanel.add(exportDescription);
        alignLabelW(labelDescription, labelW);

        return descriptionPanel;
    }

    private Panel createContentPanel() {
        exportWords = new CheckBox(msgs.includeTunedWords());
        exportWords.setValue(true);

        exportRules = new CheckBox(msgs.includeExceptionRules());
        exportRules.setValue(true);

        HorizontalPanel contentPanel = new HorizontalPanel();
        contentPanel.setBorderWidth(borderW);
        //contentPanel.setSpacing(spacing);

        HorizontalPanel contentLabelPanel = new HorizontalPanel();
        contentLabelPanel.setBorderWidth(borderW);
        contentLabelPanel.setSpacing(spacing);

        InlineLabel labelContent = new InlineLabel(msgs.exportContent());
        contentLabelPanel.add(labelContent);
        alignLabelW(labelContent, labelW - spacing);

        contentPanel.add(contentLabelPanel);

        VerticalPanel rbPanel = new VerticalPanel();
        rbPanel.setBorderWidth(borderW);

        rbPanel.add(new HTML(msgs.divNbsp()));
        rbPanel.add(exportWords);
        rbPanel.add(exportRules);

        contentPanel.add(rbPanel);
        return contentPanel;
    }

    private void alignLabelW(InlineLabel label, final int w) {
        StyleUtils.changeStyle(label.getElement(), StyleUtils.FIRST_PARENT, new StyleUtils.StyleChanger() {
            @Override
	    public void changeStyle(Style style) {
                style.setWidth(w, Style.Unit.PX);
            }
        });
    }

    private ExportPanel hiddenPanel;

    private void buildExportPanel() {
        if (null != hiddenPanel)
            hiddenPanel.removeFromParent();
        hiddenPanel = new ExportPanel("sentiment_transfer/sentiment_transfer_service/sentiment_export");

        hiddenPanel.setField(ExportPanel.SENT_EXPORT_PROJECTID, getProjectId());
        hiddenPanel.setField(ExportPanel.SENT_EXPORT_RULES, exportRules.getValue());
        hiddenPanel.setField(ExportPanel.SENT_EXPORT_WORDS, exportWords.getValue());
        hiddenPanel.setField(ExportPanel.SENT_EXPORT_NAME, exportName.getValue());
        hiddenPanel.setField(ExportPanel.SENT_EXPORT_DESCR, exportDescription.getValue());

        RootPanel.get().add(hiddenPanel); // super-important to use a RootPanel here instead of this dialog
    }

    @Override
    public void onFinish() {
        buildExportPanel();
        hiddenPanel.submit();
        super.onFinish();
    }

    @Override
    public void onClose() {
        if (finishHandler != null) {
            finishHandler.onRecentSentimentsExport();
        }
        super.onClose();
    }
}
