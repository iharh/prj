package mygwt.web.client;

import mygwt.common.security.model.PermissionName;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

import mygwt.foundation.client.widget.dialog.BaseDialogBox;
//import mygwt.web.client.sample.SampleDialog;
//import mygwt.web.client.sentiments.ExportSentimentsDialog;
//import mygwt.web.client.sentiments.rse.RecentSentimentExportsDialog;
import mygwt.web.client.sentiments.wizard.SentimentsWizard;

public class MyEntryPoint implements EntryPoint {

    private BaseDialogBox getDialogBox() {
        //return new SampleDialog();
        //return new ExportSentimentsDialog();
        //return new RecentSentimentExportsDialog();
        return new SentimentsWizard(0);
    }

    @Override
    public void onModuleLoad() {
	RootPanel.get().add(new Label("Hello: " + PermissionName.NONE.name()));
        final BaseDialogBox popup = getDialogBox();
        popup.show();
    }
}
