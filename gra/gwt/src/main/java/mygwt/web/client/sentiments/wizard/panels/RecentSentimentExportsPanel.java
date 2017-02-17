package mygwt.web.client.sentiments.wizard.panels;

import mygwt.web.client.sentiments.resources.SentimentsMessages;

import mygwt.web.client.sentiments.rse.RecentSentimentExportsServiceAsync;

import mygwt.portal.dto.sentiments.rse.RecentSentimentExportsInfo;

import mygwt.foundation.client.rpc.AbstractAsyncCallback;

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

public class RecentSentimentExportsPanel extends BasePanel {
    private static final int borderW     = 0; // 1 for borders vis-n
    private static final int spacing     = 12;
    //private static final String textBoxW = "470px"; // 450px
    //private static final int labelW      = 150;

    private SentimentsMessages msgs;

    private SingleSelectionModel<RecentSentimentExportsInfo> selectionModel;

    long projectId;
    private RecentSentimentExportsServiceAsync svcAsync;

    public RecentSentimentExportsPanel(long projectId, RecentSentimentExportsServiceAsync svcAsync) {
        super();
        this.projectId = projectId;
        this.svcAsync = svcAsync;

        msgs = SentimentsMessages.INSTANCE;

        verticalPanel.setBorderWidth(borderW);
	verticalPanel.setSpacing(spacing); // too rough

        verticalPanel.add(new InlineLabel(msgs.rseText()));

        ProvidesKey<RecentSentimentExportsInfo> keyProvider = new ProvidesKey<RecentSentimentExportsInfo>() {
            public Object getKey(RecentSentimentExportsInfo i) {
                // Always do a null check.
                return (i == null) ? null : i.getFileName();
            }
        };

        final DataGrid<RecentSentimentExportsInfo> dataGrid = new DataGrid<RecentSentimentExportsInfo>(5, keyProvider);
        //dataGrid.setPageSize(5);
	dataGrid.setSize("100%", "400px");

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
        //dataGrid.setAlwaysShowScrollBars(true);
        //http://samples.gwtproject.org/samples/Showcase/Showcase.html#!CwDataGrid

        final ListDataProvider<RecentSentimentExportsInfo> dataProvider = new ListDataProvider<RecentSentimentExportsInfo>();
        svcAsync.getExports(projectId, new AbstractAsyncCallback<List<RecentSentimentExportsInfo>>() {
            @Override
            public void onSuccess(List<RecentSentimentExportsInfo> rowData) {
                // dataGrid.setRowData(rowData); // does not preserve page size
                dataProvider.getList().addAll(rowData);
                dataProvider.addDataDisplay(dataGrid);
            }
        });

        verticalPanel.add(dataGrid);
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
}
