package mygwt.client;

import mygwt.client.sentiments.ExportSentimentsDialog;

import mygwt.foundation.client.widget.dialog.BaseDialogBox;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class MyEntryPoint implements EntryPoint {
    @Override
    public void onModuleLoad() {
	RootPanel.get().add(new Label("Hello GWT World!"));

        final BaseDialogBox popup = new ExportSentimentsDialog();
        popup.show();
    }
}
