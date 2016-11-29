package mygwt.web.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import mygwt.foundation.client.widget.dialog.BaseDialogBox;
//import mygwt.web.client.sentiments.ExportSentimentsDialog;
import mygwt.web.client.sentiments.rse.RecentSentimentExportsDialog;

public class MyEntryPoint implements EntryPoint {

    private BaseDialogBox getDialogBox() {
        //return new ExportSentimentsDialog();
        return new RecentSentimentExportsDialog();
    }

    @Override
    public void onModuleLoad() {
	RootPanel.get().add(new Label("Hello GWT World!"));
        final BaseDialogBox popup = getDialogBox();
        popup.show();
    }
}
