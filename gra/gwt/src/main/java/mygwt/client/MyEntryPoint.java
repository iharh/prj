package mygwt.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import mygwt.foundation.client.widget.dialog.BaseDialogBox;
//import mygwt.client.sentiments.ExportSentimentsDialog;
import mygwt.client.sentiments.GetPreviousSentimentsExportDialog;

public class MyEntryPoint implements EntryPoint {

    private BaseDialogBox getDialogBox() {
        //return new ExportSentimentsDialog();
        return new GetPreviousSentimentsExportDialog();
    }

    @Override
    public void onModuleLoad() {
	RootPanel.get().add(new Label("Hello GWT World!"));
        final BaseDialogBox popup = getDialogBox();
        popup.show();
    }
}
