package mygwt.client.sentiments;

import mygwt.client.sentiments.RecentSentimentExportsInfo;
import mygwt.client.sentiments.service.RecentSentimentExportsService;
import mygwt.client.sentiments.service.RecentSentimentExportsServiceAsync;

//import mygwt.foundation.client.rpc.AbstractAsyncCallback;

import mygwt.foundation.client.widget.dialog.BaseDialogBox;
import mygwt.foundation.client.widget.button.CancelButton;
import mygwt.foundation.client.widget.button.OkButton;

import mygwt.web.client.utils.StyleUtils;

import mygwt.web.client.sentiments.resources.SentimentsMessages;

import com.google.gwt.core.client.GWT;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;

import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;

import com.google.gwt.user.client.Window;

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

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.Arrays;
import java.util.List;


public class RecentSentimentExportsDialog extends BaseDialogBox {
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

    //private ExportPanel hiddenPanel;
    private SentimentsMessages msgs;

    public RecentSentimentExportsDialog() {
        super(SentimentsMessages.INSTANCE.rceTitle(), allW, allH);
        msgs = SentimentsMessages.INSTANCE;
        setWidget(createDialogContents());
        hide();
    }

    private Widget createDialogContents() {
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
        //infoPanel.setBorderWidth(borderW);
	infoPanel.setSpacing(spacing); // too rough

        infoPanel.add(new InlineLabel(msgs.rceText()));

        final DataGrid<RecentSentimentExportsInfo> dataGrid = new DataGrid<RecentSentimentExportsInfo>();
	dataGrid.setSize("100%", "400px");

	dataGrid.setEmptyTableWidget(new HTML(msgs.rceNoExportsDefined()));

        TextColumn<RecentSentimentExportsInfo> colId = new TextColumn<RecentSentimentExportsInfo>() {
            @Override public String getValue(RecentSentimentExportsInfo i) { return i.getId(); }
        };
        TextColumn<RecentSentimentExportsInfo> colDescription = new TextColumn<RecentSentimentExportsInfo>() {
            @Override public String getValue(RecentSentimentExportsInfo i) { return i.getDescription(); }
        };
        TextColumn<RecentSentimentExportsInfo> colTimestamp = new TextColumn<RecentSentimentExportsInfo>() {
            @Override public String getValue(RecentSentimentExportsInfo i) { return i.getTimestamp(); }
        };
        TextColumn<RecentSentimentExportsInfo> colParameters = new TextColumn<RecentSentimentExportsInfo>() {
            @Override public String getValue(RecentSentimentExportsInfo i) { return i.getParameters(); }
        };
        TextColumn<RecentSentimentExportsInfo> colFileName = new TextColumn<RecentSentimentExportsInfo>() {
            @Override public String getValue(RecentSentimentExportsInfo i) { return i.getFileName(); }
        };

        Unit u = Unit.PCT;
        dataGrid.addColumn(colId, msgs.rceColumnName());
	dataGrid.setColumnWidth(colId, 15, u);
        dataGrid.addColumn(colDescription, msgs.rceColumnDescription());
	dataGrid.setColumnWidth(colDescription, 20, u);
        dataGrid.addColumn(colTimestamp, msgs.rceColumnTimestamp());
	dataGrid.setColumnWidth(colTimestamp, 25, u);
        dataGrid.addColumn(colParameters, msgs.rceColumnParameters());
	dataGrid.setColumnWidth(colParameters, 20, u);
        dataGrid.addColumn(colFileName, msgs.rceColumnFile());
	dataGrid.setColumnWidth(colFileName, 20, u);

        /*getSvcAsync().getExports(0, new AsyncCallback<List<RecentSentimentExportsInfo>>() {
            @Override
            public void onSuccess(List<RecentSentimentExportsInfo> rowData) {
                dataGrid.setRowData(rowData);
            }
            public void onFailure(Throwable t) {
                Window.alert(t.getMessage());
            }
        });*/
        dataGrid.setRowData(Arrays.asList(
            new RecentSentimentExportsInfo("1", "2", "3", "4", "5")
        ));

        infoPanel.add(dataGrid);

        return infoPanel;
    }

    private RecentSentimentExportsServiceAsync svcAsync;

    public RecentSentimentExportsServiceAsync getSvcAsync() {
        if (svcAsync == null) {
            svcAsync = (RecentSentimentExportsServiceAsync) GWT.create(RecentSentimentExportsService.class);
            // injectRpcBuilder(((ServiceDefTarget) svcAsync), Service.THEME_DETECTION_SERVICE);
        }
        return svcAsync;
    }

    private Panel createButtonPanel() {
        closeButtonHandler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                RecentSentimentExportsDialog.this.hide();
            }
        };

        final OkButton     exportBtn = new OkButton(SentimentsMessages.INSTANCE.export());
        final CancelButton cancelBtn = new CancelButton(SentimentsMessages.INSTANCE.cancel());

        /*exportBtn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                buildExportPanel();
                hiddenPanel.submit();
            }
        });*/
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

    /*private void buildExportPanel() {
        if (null != hiddenPanel)
            hiddenPanel.removeFromParent();
        hiddenPanel = new ExportPanel("sentiment_export.action");

        hiddenPanel.setField(ExportPanel.SENT_EXPORT_PROJECTID, ApplicationContext.get().getProjectId());
        hiddenPanel.setField(ExportPanel.SENT_EXPORT_RULES, exportRules.getValue());
        hiddenPanel.setField(ExportPanel.SENT_EXPORT_WORDS, exportWords.getValue());
        hiddenPanel.setField(ExportPanel.SENT_EXPORT_NAME, exportName.getValue());
        hiddenPanel.setField(ExportPanel.SENT_EXPORT_DESCR, exportDescription.getValue());
        
        RootPanel.get().add(hiddenPanel);
    }*/
}
