package mygwt.web.client.sentiments.wizard.panels;

import mygwt.web.client.sentiments.resources.SentimentsMessages;

import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.RadioButton;

public class OperationSelectionPanel extends BasePanel {
    private static String BUTTON_GROUP = "OPERATION_SELECTION_GROUP";

    private SentimentsMessages msgs;

    private RadioButton btnImport;
    private RadioButton btnExportCur;
    private RadioButton btnExportPrev;

    public OperationSelectionPanel() {
        super(null);

        msgs = SentimentsMessages.INSTANCE;

        //addStyleName("myOperationSelectionPanel");
        add(new InlineLabel(msgs.operationSelectionLabel()));

        btnImport = new RadioButton(BUTTON_GROUP, msgs.choiceImport());
        btnExportCur = new RadioButton(BUTTON_GROUP, msgs.choiceExportCurrent());
        btnExportPrev = new RadioButton(BUTTON_GROUP, msgs.choiceExportRecent());

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
