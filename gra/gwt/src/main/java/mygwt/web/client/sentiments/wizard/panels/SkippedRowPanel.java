package mygwt.web.client.sentiments.wizard.panels;

import mygwt.portal.dto.SkippedInfo;

import mygwt.web.client.sentiments.upload.resources.SentimentUploadMessages;

import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.List;

public class SkippedRowPanel extends VerticalPanel {
    private static final String DIALOG_WIDTH = "510px";
    private static final String CELL_HEIGHT = "145px";

    private HTML title = new HTML();
    private SkippedRowViz skipperRowViz;

    public SkippedRowPanel(String hintTitle) {
        setSize(DIALOG_WIDTH, CELL_HEIGHT);
        add(title);
        skipperRowViz = new SkippedRowViz(hintTitle);
        skipperRowViz.setVisible(false);
        add(skipperRowViz);
        // Adjust height of grid
        setCellHeight(skipperRowViz, CELL_HEIGHT);
    }

    public void fillData(List<SkippedInfo> results) {
        skipperRowViz.fillData(results);
    }
	
    @Override
    public void setVisible(boolean visible) {
        skipperRowViz.setVisible(visible);
        super.setVisible(visible);
    }

    private static class SkippedRowViz extends ScrollTablePanel {
        private static final String GRID_WIDTH = "500px"; //$NON-NLS-1$

        private static class ContentBuilder extends ScrollTablePanel.ContentBuilder {
            private final String hintTitle;
            
            public ContentBuilder(String hintTitle) {
                this.hintTitle = hintTitle;
            }

            @Override
            public final FixedWidthFlexTable createHeaderTable() {
                FixedWidthFlexTable headerTable = super.createHeaderTable();
                headerTable.setHTML(0, 0, SentimentUploadMessages.INSTANCE.rowNumber());
                headerTable.setHTML(0, 1, hintTitle);
                headerTable.setHTML(0, 2, SentimentUploadMessages.INSTANCE.errorMessage());
                headerTable.setColumnWidth(0, 46);
                headerTable.setColumnWidth(1, 46);
                headerTable.setColumnWidth(2, 370);
                return headerTable;
            }
        }

        public SkippedRowViz(String hintTitle) {
            super(GRID_WIDTH, CELL_HEIGHT, new ContentBuilder(hintTitle));
        }
            
        public void fillData(List<SkippedInfo> rows) {
            FixedWidthGrid grid = getDataTable();
            grid.clear();
            int ridx = 0;
            grid.resize(rows.size(), 3);
            
            for (SkippedInfo r : rows) {
                grid.setHTML(ridx, 0, Integer.toString(r.getRowNum()));
                grid.setHTML(ridx, 1, r.getHint());
                grid.setHTML(ridx, 2, r.getErrMessage());
                ++ridx;
            }
            getScrollTable().setSize(GRID_WIDTH, CELL_HEIGHT);
            getScrollTable().setColumnWidth(0, 46);
            getScrollTable().setColumnWidth(1, 46);
            getScrollTable().setColumnWidth(2, 370);
            getScrollTable().redraw();
        }	
    }
}

