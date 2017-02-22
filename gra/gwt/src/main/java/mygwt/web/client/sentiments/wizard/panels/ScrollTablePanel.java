package mygwt.web.client.sentiments.wizard.panels;

import com.google.gwt.gen2.table.client.AbstractScrollTable.ResizePolicy;
import com.google.gwt.gen2.table.client.AbstractScrollTable.SortPolicy;
import com.google.gwt.gen2.table.client.FixedWidthFlexTable;
import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.gen2.table.client.ScrollTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public abstract class ScrollTablePanel extends VerticalPanel {
    private FixedWidthFlexTable headerTable;
    private FixedWidthGrid dataTable;
    private ScrollTable scrollTable;

    protected static class ContentBuilder {
        public void init(ScrollTablePanel dst) {
            dst.headerTable = createHeaderTable();
            dst.dataTable = createDataTable();

            // Combine the components into a ScrollTable
            dst.scrollTable = new ScrollTable(dst.dataTable, dst.headerTable);
            dst.scrollTable.setFooterTable(createFooterTable());

            // Set some options in the scroll table
            dst.scrollTable.setResizePolicy(ScrollTable.ResizePolicy.UNCONSTRAINED);
            dst.scrollTable.setSize("100%", "100%");
            dst.scrollTable.setScrollPolicy(ScrollTable.ScrollPolicy.BOTH);
            dst.scrollTable.setSortPolicy(SortPolicy.DISABLED);

            dst.scrollTable.setStylePrimaryName("gwt-ExcelTable");
            dst.scrollTable.setVisible(false);

            dst.setCellHorizontalAlignment(dst.scrollTable, HorizontalPanel.ALIGN_LEFT);			
        }

        public FixedWidthFlexTable createFooterTable() {
            FixedWidthFlexTable footerTable = new FixedWidthFlexTable();
            return footerTable;
        }

        public FixedWidthFlexTable createHeaderTable() {
            FixedWidthFlexTable result = new FixedWidthFlexTable();
            return result;
        }

        public FixedWidthGrid createDataTable() {
            FixedWidthGrid result = new FixedWidthGrid();
            result.setBorderWidth(1);
            return result;
        }
    }

    public ScrollTablePanel(String width, String height, ContentBuilder contentBuilder) {
        super();
        setSize(width, height);
        contentBuilder.init(this);
        this.add(scrollTable);
    }

    public void clearTable() {
        if (scrollTable != null) {
            scrollTable.getDataTable().clear();
            scrollTable.redraw();
        }
    }

    @Override
    public void setVisible(boolean visible) {
        scrollTable.setVisible(visible);
        dataTable.setVisible(visible);
        headerTable.setVisible(visible);
        super.setVisible(visible);
    }

    public FixedWidthFlexTable getHeaderTable() {
        return headerTable;
    }

    public FixedWidthGrid getDataTable() {
        return dataTable;
    }

    public ScrollTable getScrollTable() {
        return scrollTable;
    }
    
    public void setResizePolicy(ResizePolicy pol) {
        scrollTable.setResizePolicy(pol);
    }
}
