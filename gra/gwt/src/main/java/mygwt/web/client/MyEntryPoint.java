package mygwt.web.client;

import mygwt.web.client.sentiments.wizard.SentimentsWizard;
import mygwt.web.client.sentiments.wizard.FinishHandler;
//import mygwt.foundation.client.widget.dialog.BaseDialogBox;
//import mygwt.web.client.sample.SampleDialog;
//import mygwt.web.client.sentiments.ExportSentimentsDialog;
//import mygwt.web.client.sentiments.rse.RecentSentimentExportsDialog;
import mygwt.foundation.client.widget.button.OkButton;

import mygwt.web.client.utils.LogUtils;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;


public class MyEntryPoint implements EntryPoint, FinishHandler {
    private SentimentsWizard popup; // BaseDialogBox

    //private BaseDialogBox getDialogBox() {
        //return new SampleDialog();
        //return new ExportSentimentsDialog();
        //return new RecentSentimentExportsDialog();
    //}

    @Override
    public void onModuleLoad() {
        popup = new SentimentsWizard(this);

        OkButton btnDo = new OkButton("Do"); // TODO: i18n
        btnDo.getElement().setId("btnDo");
        btnDo.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                popup.startFromFirstStep();
                popup.show();
            }
        });

	RootPanel.get().add(btnDo);
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
