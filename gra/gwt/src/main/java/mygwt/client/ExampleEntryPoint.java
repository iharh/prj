package mygwt.client;

import mygwt.client.sentiments.ExportSentimentsDialog;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

//import com.google.gwt.uibinder.client.UiField;
//import com.google.gwt.uibinder.client.UiHandler;

public class ExampleEntryPoint implements EntryPoint {
    private ExportSentimentsDialog exportPopup;

    @Override
    public void onModuleLoad() {
	RootPanel.get().add(new Label("Hello GWT World!"));

        exportPopup = new ExportSentimentsDialog();
        exportPopup.show();
    }
/*
    @UiHandler("exportButton")
    protected void handleExportButton(ClickEvent event) {
        if (exportPopup == null) {
            exportPopup = new ExportSentimentsDialog();
        }
        exportPopup.show();
    }
*/
}
