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
import mygwt.web.client.sentiments.wizard.FinishHandler;

import mygwt.web.client.utils.LogUtils;

public class MyEntryPoint implements EntryPoint, FinishHandler {
    private BaseDialogBox popup;

    private BaseDialogBox getDialogBox() {
        //return new SampleDialog();
        //return new ExportSentimentsDialog();
        //return new RecentSentimentExportsDialog();
        return new SentimentsWizard(this);
    }

    @Override
    public void onModuleLoad() {
	RootPanel.get().add(new Label("Hello: " + PermissionName.NONE.name()));
        popup = getDialogBox();
        popup.show();
    }

    @Override
    public void onImport(boolean updateSentences) {
        LogUtils.log("onImport updateSentences: " + updateSentences);
/*
        sentimentTab.refreshSentimentTable(false);
        sentimentTab.refreshExceptionRulesList();
        if (updateSentences) {
            sentimentTab.recalculateSentiments();
        } else {
            sentimentTab.setRecalculateVisible(true);
        }
*/
        popup.hide();
    }

    @Override
    public void onRecentSentimentsExport() {
        LogUtils.log("onRecentSentimentsExport");
        popup.hide();
    }

    @Override
    public void onCurrentExport() {
        LogUtils.log("onCurrentExport");
        popup.hide();
    }
}
