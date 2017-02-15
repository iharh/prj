package mygwt.web.client.sentiments;

import mygwt.common.client.context.ApplicationContext;

import mygwt.web.client.report.ExportPanel;

import mygwt.foundation.client.widget.dialog.BaseDialogBox;
import mygwt.foundation.client.widget.button.CancelButton;
import mygwt.foundation.client.widget.button.OkButton;

import mygwt.web.client.utils.StyleUtils;

import mygwt.web.client.sentiments.resources.SentimentsMessages;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;

//import com.google.gwt.gen2.table.client.FixedWidthFlexTable;

public class ExportSentimentsDialog extends BaseDialogBox {
    private static final int borderW     = 0; // 1 for borders vis-n
    private static final int allW        = 640;
    private static final int allH        = 370;
    private static final String textBoxW = "470px"; // 450px
    private static final int labelW      = 150;
    private static final int spacing     = 12;

    private CheckBox exportWords;
    private CheckBox exportRules;
    private TextBox exportName;
    private TextArea exportDescription;

    private ClickHandler closeButtonHandler;

    private ExportPanel hiddenPanel;

    public ExportSentimentsDialog() {
        super(SentimentsMessages.INSTANCE.sentimentExport(), allW, allH);
        setWidget(createDialogContents());
        hide();
    }

    protected Widget createDialogContents() {
        DockLayoutPanel panel = new DockLayoutPanel(Unit.PX);

        panel.setWidth(allW + "px");
        panel.setHeight(allH + "px");

        panel.addNorth(createExportInfoPanel(), 250);
        panel.addSouth(createButtonPanel(), 50);

        return panel;
    }

    private Panel createExportInfoPanel() {
        VerticalPanel infoPanel = new VerticalPanel();
        infoPanel.setBorderWidth(borderW);
	// infoPanel.setSpacing(spacint); // too rough

        infoPanel.add(createNamePanel());
        infoPanel.add(createDescriptionPanel());
        infoPanel.add(createContentPanel());

        return infoPanel;
    }

    private Panel createNamePanel() {
        HorizontalPanel namePanel = new HorizontalPanel();
        namePanel.setBorderWidth(borderW);
        namePanel.setSpacing(spacing);

        InlineLabel labelName = new InlineLabel(SentimentsMessages.INSTANCE.exportName());
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

        InlineLabel labelDescription = new InlineLabel(SentimentsMessages.INSTANCE.exportDescription());
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
        exportWords = new CheckBox(SentimentsMessages.INSTANCE.includeTunedWords());
        exportWords.setValue(true);

        exportRules = new CheckBox(SentimentsMessages.INSTANCE.includeExceptionRules());
        exportRules.setValue(true);

        HorizontalPanel contentPanel = new HorizontalPanel();
        contentPanel.setBorderWidth(borderW);
        //contentPanel.setSpacing(spacing);

        HorizontalPanel contentLabelPanel = new HorizontalPanel();
        contentLabelPanel.setBorderWidth(borderW);
        contentLabelPanel.setSpacing(spacing);

        InlineLabel labelContent = new InlineLabel(SentimentsMessages.INSTANCE.exportContent());
        contentLabelPanel.add(labelContent);
        alignLabelW(labelContent, labelW - spacing);

        contentPanel.add(contentLabelPanel);

        VerticalPanel rbPanel = new VerticalPanel();
        rbPanel.setBorderWidth(borderW);

        rbPanel.add(new HTML(SentimentsMessages.INSTANCE.divNbsp()));
        rbPanel.add(exportWords);
        rbPanel.add(exportRules);

        contentPanel.add(rbPanel);
        return contentPanel;
    }

    private Panel createButtonPanel() {
        closeButtonHandler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ExportSentimentsDialog.this.hide();
            }
        };

        final OkButton     exportBtn = new OkButton(SentimentsMessages.INSTANCE.export());
        final CancelButton cancelBtn = new CancelButton(SentimentsMessages.INSTANCE.cancel());

        exportBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                buildExportPanel();
                hiddenPanel.submit();
            }
        });

        exportBtn.addClickHandler(closeButtonHandler);
        cancelBtn.addClickHandler(closeButtonHandler);

        DockLayoutPanel btnPanel = new DockLayoutPanel(Unit.PX);
        btnPanel.setWidth(allW + "px");
        btnPanel.setHeight("50px");

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setBorderWidth(borderW);

        buttonPanel.add(exportBtn);
        buttonPanel.add(new HTML(SentimentsMessages.INSTANCE.nbsp()));
        buttonPanel.add(cancelBtn);

        btnPanel.addEast(buttonPanel, 165);

        return btnPanel;
    }

    private void alignLabelW(InlineLabel label, final int w) {
        StyleUtils.changeStyle(label.getElement(), StyleUtils.FIRST_PARENT, new StyleUtils.StyleChanger() {
            @Override
	    public void changeStyle(Style style) {
                style.setWidth(w, Style.Unit.PX);
            }
        });
    }

    private void buildExportPanel() {
        if (null != hiddenPanel)
            hiddenPanel.removeFromParent();
        hiddenPanel = new ExportPanel("sentiment_export.action");

        hiddenPanel.setField(ExportPanel.SENT_EXPORT_PROJECTID, ApplicationContext.get().getProjectId());
        hiddenPanel.setField(ExportPanel.SENT_EXPORT_RULES, exportRules.getValue());
        hiddenPanel.setField(ExportPanel.SENT_EXPORT_WORDS, exportWords.getValue());
        hiddenPanel.setField(ExportPanel.SENT_EXPORT_NAME, exportName.getValue());
        hiddenPanel.setField(ExportPanel.SENT_EXPORT_DESCR, exportDescription.getValue());
        
        RootPanel.get().add(hiddenPanel);
    }
}
