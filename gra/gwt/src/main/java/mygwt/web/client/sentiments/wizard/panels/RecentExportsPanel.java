package mygwt.web.client.sentiments.wizard.panels;

import mygwt.web.client.sentiments.wizard.FinishHandler;

import mygwt.web.client.sentiments.resources.SentimentsMessages;

import mygwt.common.client.service.RecentSentimentExportsServiceAsync;

import mygwt.web.client.report.ExportPanel;

import mygwt.web.client.utils.LogUtils;

import mygwt.portal.dto.sentiments.rse.RecentSentimentExportsInfo;

import mygwt.foundation.client.rpc.AbstractAsyncCallback;
import mygwt.foundation.client.csrf.ProjectIdAware;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;

import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;

import java.util.List;
import java.util.LinkedList;

public class RecentExportsPanel extends BasePanel {
    private FinishHandler finishHandler;

    private SentimentsMessages msgs;

    private DataGrid<RecentSentimentExportsInfo> dataGrid;
    private SingleSelectionModel<RecentSentimentExportsInfo> selectionModel;

    private ListDataProvider<RecentSentimentExportsInfo> dataProvider;

    private RecentSentimentExportsServiceAsync svcAsync;

    public RecentExportsPanel(ProjectIdAware projectIdAware, FinishHandler finishHandler, RecentSentimentExportsServiceAsync svcAsync) {
        super(projectIdAware);
        this.finishHandler = finishHandler;
        this.svcAsync = svcAsync;

        msgs = SentimentsMessages.INSTANCE;

        createMainContent();
    }

    private void createMainContent() {
        add(new InlineLabel(msgs.rseText())); //addStyleName("myRecentSentimentExportsPanel");

        ProvidesKey<RecentSentimentExportsInfo> keyProvider = new ProvidesKey<RecentSentimentExportsInfo>() {
            public Object getKey(RecentSentimentExportsInfo i) {
                return (i == null) ? null : i.getFileName(); // Always do a null check.
            }
        };

        dataGrid = new DataGrid<RecentSentimentExportsInfo>(keyProvider); // default page size is 50
	dataGrid.setSize("100%", "250px"); // 100% 350px

	dataGrid.setEmptyTableWidget(new HTML(msgs.rseNoExportsDefined()));

        TextColumn<RecentSentimentExportsInfo> colName = new TextColumn<RecentSentimentExportsInfo>() {
            @Override public String getValue(RecentSentimentExportsInfo i) { return i.getName(); }
        };
        TextColumn<RecentSentimentExportsInfo> colTimestamp = new TextColumn<RecentSentimentExportsInfo>() {
            @Override public String getValue(RecentSentimentExportsInfo i) { return i.getTimestamp(); }
        };
        TextColumn<RecentSentimentExportsInfo> colParameters = new TextColumn<RecentSentimentExportsInfo>() {
            @Override public String getValue(RecentSentimentExportsInfo i) { return getParams(i.isWords(), i.isRules()); }
        };
        TextColumn<RecentSentimentExportsInfo> colFileName = new TextColumn<RecentSentimentExportsInfo>() {
            @Override public String getValue(RecentSentimentExportsInfo i) { return i.getFileName(); }
        };

        Unit u = Unit.PCT;
        dataGrid.addColumn(colName, msgs.rseColumnName());
	dataGrid.setColumnWidth(colName, 25, u);
        dataGrid.addColumn(colTimestamp, msgs.rseColumnTimestamp());
	dataGrid.setColumnWidth(colTimestamp, 30, u);
        dataGrid.addColumn(colParameters, msgs.rseColumnParameters());
	dataGrid.setColumnWidth(colParameters, 20, u);
        dataGrid.addColumn(colFileName, msgs.rseColumnFile());
	dataGrid.setColumnWidth(colFileName, 25, u);

        selectionModel = new SingleSelectionModel<RecentSentimentExportsInfo>(keyProvider);

        dataGrid.setSelectionModel(selectionModel);
        dataProvider = new ListDataProvider<RecentSentimentExportsInfo>();
        dataProvider.setList(new LinkedList<RecentSentimentExportsInfo>());
        dataProvider.addDataDisplay(dataGrid);

        add(dataGrid);
        LogUtils.log("done adding");
    }

    private String getParams(boolean words, boolean rules) {
        StringBuilder result = new StringBuilder();
        if (words) {
            result.append(",");
            result.append(msgs.rseWords());
        }
        if (rules) {
            result.append(",");
            result.append(msgs.rseRules());
        }
        return result.length() > 0 ? result.substring(1) : result.toString();
    }

    @Override
    public void onEnter() {
        super.onEnter();
        svcAsync.getExports(getProjectId(), new AbstractAsyncCallback<List<RecentSentimentExportsInfo>>() {
            @Override
            public void onSuccess(List<RecentSentimentExportsInfo> rowData) {
                dataProvider.getList().clear();
                dataProvider.getList().addAll(rowData);
                dataProvider.refresh();
            }
        });
    }

    private ExportPanel hiddenPanel;

    private void buildExportPanel() {
        if (null != hiddenPanel)
            hiddenPanel.removeFromParent();
        hiddenPanel = new ExportPanel("sentiment_transfer/sentiment_transfer_service/latest_sentiment_exports");

        hiddenPanel.setField(ExportPanel.SENT_EXPORT_PROJECTID, getProjectId());

        RecentSentimentExportsInfo info = selectionModel.getSelectedObject();
        hiddenPanel.setField(ExportPanel.EXPORT_ID, info.getFileName());
        
        add(hiddenPanel);
    }

    @Override
    public void onFinish() {
        buildExportPanel();
        hiddenPanel.submit();

        LogUtils.log("1");
        if (finishHandler != null) {
            finishHandler.onRecentSentimentsExport();
            LogUtils.log("2");
        }
        LogUtils.log("3");
        super.onFinish();
    }
}
