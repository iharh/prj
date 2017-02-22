package mygwt.web.client.sentiments.wizard.panels;

import mygwt.web.client.sentiments.resources.SentimentsMessages;

import mygwt.web.client.sentiments.rse.RecentSentimentExportsServiceAsync;

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
    private SentimentsMessages msgs;

    private DataGrid<RecentSentimentExportsInfo> dataGrid;
    private SingleSelectionModel<RecentSentimentExportsInfo> selectionModel;

    private ListDataProvider<RecentSentimentExportsInfo> dataProvider;

    private RecentSentimentExportsServiceAsync svcAsync;

    public RecentExportsPanel(ProjectIdAware projectIdAware, RecentSentimentExportsServiceAsync svcAsync) {
        super(projectIdAware);
        this.svcAsync = svcAsync;

        msgs = SentimentsMessages.INSTANCE;

        createMainContent();
    }

    private void createMainContent() {
        //addStyleName("myRecentSentimentExportsPanel");
        add(new InlineLabel(msgs.rseText()));

        ProvidesKey<RecentSentimentExportsInfo> keyProvider = new ProvidesKey<RecentSentimentExportsInfo>() {
            public Object getKey(RecentSentimentExportsInfo i) {
                // Always do a null check.
                return (i == null) ? null : i.getFileName();
            }
        };

        dataGrid = new DataGrid<RecentSentimentExportsInfo>(keyProvider); // default page size is 50
	dataGrid.setSize("100%", "150px"); // 100% 350px

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
        dataProvider = new ListDataProvider<RecentSentimentExportsInfo>();
        dataProvider.setList(new LinkedList<RecentSentimentExportsInfo>());
        dataProvider.addDataDisplay(dataGrid);

        add(dataGrid);
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
                // dataGrid.setRowData(rowData); // does not preserve page size
                //dataGrid.redraw();
            }
        });
    }
}
