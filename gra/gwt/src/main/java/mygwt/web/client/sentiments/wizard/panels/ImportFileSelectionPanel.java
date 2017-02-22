package mygwt.web.client.sentiments.wizard.panels;

import mygwt.web.client.sentiments.wizard.ImportModel;

import mygwt.web.client.sentiments.wizard.panels.ButtonsPanel;

import mygwt.web.client.sentiments.upload.SentimentUploadException;
import mygwt.web.client.sentiments.upload.resources.SentimentUploadMessagesHelper;

import mygwt.foundation.client.exception.ServiceException;

import mygwt.common.client.service.SentimentUploadServiceAsync;
import mygwt.common.client.widget.dialog.MessageDialog;

import mygwt.web.client.sentiments.upload.resources.SentimentUploadMessages;
import mygwt.web.client.utils.LogUtils;

import mygwt.portal.dto.SentimentUploadValidationResult;
import mygwt.portal.dto.SentimentUploadConstants;

import mygwt.foundation.client.csrf.CsrfFormPanel;
import mygwt.foundation.client.rpc.AbstractAsyncCallback;
import mygwt.foundation.client.widget.AjaxLoaderImage;
import mygwt.foundation.client.widget.dialog.SessionExpiredDialog;
import mygwt.foundation.client.widget.dialog.YesNoDialog;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Hidden;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;

public class ImportFileSelectionPanel extends BasePanel {
    private static final String ERROR_MESSAGE_STYLE = "errorMessage";

    private SentimentUploadMessages msgs;

    private ImportModel importModel;
    private ButtonsPanel buttonsPanel;
    private StepNavigator stepNavigator;
    private SentimentUploadServiceAsync sentimentService;

    private Image wheel;
    private HTML statusLabel;
    private FormPanel form;
    private Hidden projIdHidden;
    private FileUpload upload;

    private String sentFileName;
    private boolean waitingFileUploadValidationResults;

    public ImportFileSelectionPanel(ImportModel importModel, ButtonsPanel buttonsPanel, StepNavigator stepNavigator, SentimentUploadServiceAsync sentimentService) {
        super();
        this.importModel = importModel;
        this.buttonsPanel = buttonsPanel;
        this.StepNavigator = stepNavigator;
        this.sentimentService = sentimentService;

        msgs = SentimentUploadMessages.INSTANCE;

        createMainContent();
    }

    private void createMainContent() {
        wheel = new AjaxLoaderImage();

        sentFileName = "";
	form = new CsrfFormPanel();
        form.setAction(GWT.getHostPageBaseURL() + "sentiment_upload.action");
        // Because we're going to add a FileUpload widget, we'll need to set the
        // form to use the POST method, and multipart MIME encoding.
        form.setEncoding(FormPanel.ENCODING_MULTIPART);

        VerticalPanel northPanel = new VerticalPanel();
        northPanel.setSize("100%", "100%"); 
        //northPanel.setBorderWidth(0);  //remove
        northPanel.setStyleName("AdHocPopupNorthPanel");

        VerticalPanel header = new VerticalPanel();
        header.add(new HTML(msgs.fileSelectionHeader()));

        header.setWidth("100%");
        header.setStyleName("headLine");
        northPanel.add(header);
        northPanel.setCellHeight(header, "30px");

        HorizontalPanel namePanel = new HorizontalPanel();
        namePanel.setWidth("100%");
        HTML help = new HTML(msgs.helpHtml());
        namePanel.add(help);

        northPanel.add(namePanel);

        VerticalPanel selectionPanel = new VerticalPanel();
        selectionPanel.setWidth("100%");
        HTML please = new HTML(msgs.pleaseHtml());
        selectionPanel.add(please);
        projIdHidden = new Hidden("projectId");
        selectionPanel.add(projIdHidden);

        upload = new FileUpload();
        upload.setWidth("100%");
        upload.setName("upload");
        upload.setStyleName("uploadBox");
        upload.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                statusLabel.removeStyleName(ERROR_MESSAGE_STYLE);
                statusLabel.setText("");
                buttonsPanel.enableNext();
                sentFileName = "";
            }
        });
        selectionPanel.add(upload);

        HorizontalPanel statusPanel = new HorizontalPanel();
        wheel.setVisible(false);
        statusLabel = new HTML();
        statusPanel.add(statusLabel);
        statusPanel.add(wheel);
        selectionPanel.add(statusPanel);

        form.add(selectionPanel);

        // Add an event handler to the form.
        form.addSubmitHandler(new SubmitHandler());
        form.addSubmitCompleteHandler(new SubmitCompleteHandler());

        northPanel.add(form);

        // for sentiment upload, base is a DockPanel  super.add(northPanel, DockPanel.NORTH)
        add(northPanel); 
    }

    private final class SubmitHandler implements FormPanel.SubmitHandler {
        @Override
        public void onSubmit(SubmitEvent event) {
            projIdHidden.setValue(String.valueOf(getProjectId()));
            sentFileName = upload.getFilename();
            statusLabel.removeStyleName(ERROR_MESSAGE_STYLE);
            if (validateForm()) {
                statusLabel.setText(msgs.fileUploadingMess());
                buttonsPanel.disableNext();
                wheel.setVisible(true);
                waitingFileUploadValidationResults = true;
            } else {
                event.cancel();
            }
        }
    }

    private final class SubmitCompleteHandler implements FormPanel.SubmitCompleteHandler {
        @Override
        public void onSubmitComplete(SubmitCompleteEvent event) {
            processFormComplete(event);
        }
    }

    private boolean validateForm() {
        boolean result = true;
        if (upload.getFilename().length() == 0) {
            MessageDialog.show(msgs.pleaseSelectMsg());
            result = false;
        }
        return result;
    }

    private void processFormComplete(SubmitCompleteEvent event) {
        wheel.setVisible(false);
        waitingFileUploadValidationResults = false;

        String error = event.getResults();

        if (error == null || error.isEmpty()) {
            String statusLab = msgs.fetchingSampleDataMess();
            clearStatusLabel(statusLab);
            getPreliminaryUploadResults();
        } else if (error.contains("j_spring_security_check")) {
            SessionExpiredDialog.showDialog();
        } else {
            statusLabel.addStyleName(ERROR_MESSAGE_STYLE);
            statusLabel.setText(error);
            sentFileName = "";
        }
    }

    private void getPreliminaryUploadResults() {
        try {
            sentimentService.getPreliminaryUploadResults(
                new AbstractAsyncCallback<SentimentUploadValidationResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        String errorMessage = msgs.fetchSampleDataErrorMess();
                        wheel.setVisible(false);
                        buttonsPanel.enableNext();
                        if (caught instanceof SentimentUploadException) {
                            errorMessage += handleException((SentimentUploadException) caught);
                        } else {
                            super.onFailure(caught);
                        }
                        statusLabel.addStyleName(ERROR_MESSAGE_STYLE);
                        statusLabel.setHTML(errorMessage);
                        GWT.log("Failed to retrieve upload validation result", caught);
                    }

                    @Override
                    public void onSuccess(SentimentUploadValidationResult result) {
                        importModel.setSentimentUploadValidationResult(result);
                        if (result.isNegatorTuned()) {
                            YesNoDialog dialog = new YesNoDialog(msgs.warning(), msgs.fileUploadNegatorTuned(),
                                new ClickHandler() {
                                    @Override
                                    public void onClick(ClickEvent event) {
                                        statusLabel.setHTML(msgs.statusSuccess());
                                        showUploadResults();
                                    }
                                }, new ClickHandler() {
                                    @Override
                                    public void onClick(ClickEvent event) {
                                        MessageDialog.showMessage(msgs.fileUploadNegatorTunedCancel());
                                        // wizard.onCancel();
                                        clearSession();
                                        stepNavigator.onBack();
                                    }
                            });
                            dialog.show();
                        } else {
                            statusLabel.setHTML(msgs.statusSuccess());
                            showUploadResults();
                        }
                    }
            });
        } catch (ServiceException e) {
            statusLabel.setHTML(msgs.statusFail() + e.getLocalizedMessage());
        }
    }

    private void showUploadResults() {
        if (importModel.saveDataToModel()) {
            stepNavigator.onNext();
        }
    }

    private void clearStatusLabel(String text) {
        statusLabel.removeStyleName(ERROR_MESSAGE_STYLE);
        statusLabel.setHTML(text);
    }

    private boolean needToSend() {
        return !waitingFileUploadValidationResults && !upload.getFilename().equals(sentFileName);
    }

    private boolean saveDataToModel() {
        if (needToSend()) {
            importModel.setSentimentUploadValidationResult(null);
            form.submit();
            return false;
        }
        return importModel.getSentimentUploadValidationResult() != null;
    }

    @Override
    public void onEnter() {
        super.onEnter();
        clearStatusLabel(msgs.selectFileMess());
        // buttonsPanel.disableNext(); // TODO: uncomment after implementing all the upload panels
    }

    // probably need to share this stuff

    private void clearSession() {
        sentimentService.cleanupSentimentsWithUploadedData(getProjectId(), AbstractAsyncCallback.VOID_CALLBACK);
    }

    private static String handleException(SentimentUploadException caught) {
        String result = "<br/>";
        SentimentUploadException e = (SentimentUploadException) caught;
        String mess = SentimentUploadMessagesHelper.getMessage(e);
        result += (mess != null && !mess.isEmpty()) ? mess : e.getLocalizedMessage();
        return result;
    }
}
