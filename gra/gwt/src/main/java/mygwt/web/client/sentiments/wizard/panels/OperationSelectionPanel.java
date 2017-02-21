package mygwt.web.client.sentiments.wizard.panels;

import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.RadioButton;

public class OperationSelectionPanel extends BasePanel {
    private static String BUTTON_GROUP = "OPERATION_SELECTION_GROUP";

    private RadioButton btnImport;
    private RadioButton btnExportCur;
    private RadioButton btnExportPrev;

    public OperationSelectionPanel() {
        super();
        //addStyleName("myOperationSelectionPanel");
        add(new InlineLabel("Please choose an option for managing your sentiment tunings."));

        btnImport = new RadioButton(BUTTON_GROUP, "Import sentiment from a Microsoft Excel file on your computer");
        btnExportCur = new RadioButton(BUTTON_GROUP, "Export current sentiment to your computer");
        btnExportPrev = new RadioButton(BUTTON_GROUP, "Download previous sentiment export to your computer");

        // Check 'First' by default.
        btnImport.setValue(true);

        add(btnImport);
        add(btnExportCur);
        add(btnExportPrev);
    }

    public boolean isImportSelected() {
        return btnImport.getValue();
    }
    public boolean isExportCurSelected() {
        return btnExportCur.getValue();
    }
    public boolean isExportPrevSelected() {
        return btnExportPrev.getValue();
    }
}
