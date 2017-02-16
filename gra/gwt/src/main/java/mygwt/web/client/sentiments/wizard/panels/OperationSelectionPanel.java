package mygwt.web.client.sentiments.wizard.panels;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RadioButton;

public class OperationSelectionPanel extends BasePanel {
    private static String BUTTON_GROUP = "OPERATION_SELECTION_GROUP";

    private RadioButton btnImport;
    private RadioButton btnExportCur;
    private RadioButton btnExportPrev;

    public OperationSelectionPanel() {
        super();

        Label lab = new Label("Please choose an option for managing your sentiment tunings.");        

        btnImport = new RadioButton(BUTTON_GROUP, "Import sentiment from a Microsoft Excel file on your computer");
        btnExportCur = new RadioButton(BUTTON_GROUP, "Export current sentiment to your computer");
        btnExportPrev = new RadioButton(BUTTON_GROUP, "Download previous sentiment export to your computer");

        // Check 'First' by default.
        btnImport.setValue(true);

        verticalPanel.add(lab);
        verticalPanel.add(btnImport);
        verticalPanel.add(btnExportCur);
        verticalPanel.add(btnExportPrev);
    }
}
