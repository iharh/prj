package mygwt.web.client.sentiments.wizard.panels;

import mygwt.web.client.sentiments.upload.SentimentUploadException;

import mygwt.common.client.service.SentimentUploadServiceAsync;
import mygwt.common.client.widget.dialog.MessageDialog;

import mygwt.web.client.sentiments.upload.resources.SentimentUploadMessages;
import mygwt.web.client.utils.LogUtils;

import mygwt.portal.dto.SentimentUploadValidationResult;

import mygwt.foundation.client.csrf.CsrfFormPanel;
import mygwt.foundation.client.rpc.AbstractAsyncCallback;
import mygwt.foundation.client.widget.AjaxLoaderImage;
import mygwt.foundation.client.widget.dialog.SessionExpiredDialog;
import mygwt.foundation.client.widget.dialog.YesNoDialog;

import mygwt.foundation.client.exception.ServiceException;

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

import java.util.Map;
import java.util.HashMap;

public class SentimentImportFileSelectionPanel extends BasePanel {
    private static final String ERROR_MESSAGE_STYLE = "errorMessage";

    private SentimentUploadMessages msgs;

    private long projectId;
    private SentimentUploadServiceAsync sentimentService;

    private Image wheel;
    private HTML statusLabel;
    private FormPanel form;
    private Hidden projIdHidden;
    private FileUpload upload;

    private String sentFileName;
    private boolean waitingFileUploadValidationResults;

    public SentimentImportFileSelectionPanel(long projectId, SentimentUploadServiceAsync sentimentService) {
        super();
        this.projectId = projectId;
        this.sentimentService = sentimentService;

        msgs = SentimentUploadMessages.INSTANCE;

        createMainContent();
    }

    private void createMainContent() {
        wheel = new AjaxLoaderImage();

        sentFileName = "";
	form = new CsrfFormPanel();
        form.setAction(GWT.getHostPageBaseURL() + "sentiment_upload.action"); //$NON-NLS-1$
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
                // getWizard().getButtonsPanel().enableNext(); // TODO: TBD
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
            projIdHidden.setValue(String.valueOf(projectId)); // TODO: FileSelectionPanel.this.getWizard().getProjectId())
            sentFileName = upload.getFilename();
            statusLabel.removeStyleName(ERROR_MESSAGE_STYLE);
            if (validateForm()) {
                statusLabel.setText(msgs.fileUploadingMess());
                //getWizard().getButtonsPanel().disableNext(); // TODO: TBD
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
            //TODO set appropriate message.
            String statusLab = msgs.fetchingSampleDataMess();
            clearStatusLabel(statusLab);
            getPreliminaryUploadResults();
        } else if (error.contains("j_spring_security_check")) { //$NON-NLS-1$
            SessionExpiredDialog.showDialog();
        } else {
            statusLabel.addStyleName(ERROR_MESSAGE_STYLE);
            statusLabel.setText(error);
            sentFileName = ""; //$NON-NLS-1$
        }
    }


    private void clearStatusLabel(String text) {
        statusLabel.removeStyleName(ERROR_MESSAGE_STYLE);
        statusLabel.setHTML(text);
    }

    private void getPreliminaryUploadResults() {
        try {
            sentimentService.getPreliminaryUploadResults(
                new AbstractAsyncCallback<SentimentUploadValidationResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        String errorMessage = msgs.fetchSampleDataErrorMess();
                        wheel.setVisible(false);
                        // getWizard().enableNext(); // TODO: TBD
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
                        // getWizard().getModel().setSentimentUploadValidationResult(result); // TODO: TBD
                        if (result.isNegatorTuned()){
                            YesNoDialog dialog = new YesNoDialog(msgs.warning(), msgs.fileUploadNegatorTuned(),
                                new ClickHandler() {
                                    @Override
                                    public void onClick(ClickEvent event) {
                                        statusLabel.setHTML(msgs.statusSuccess());
                                        // getWizard().showUploadResults(); // TODO: TBD
                                    }
                                }, new ClickHandler() {
                                    @Override
                                    public void onClick(ClickEvent event) {
                                        MessageDialog.showMessage(msgs.fileUploadNegatorTunedCancel());
                                        // getWizard().onCancel(); // TODO: TBD
                                    }
                            });
                            dialog.show();
                        } else {
                            statusLabel.setHTML(msgs.statusSuccess());
                            // getWizard().showUploadResults(); // TODO: TBD
                        }
                    }
            });
        } catch (ServiceException e) {
            statusLabel.setHTML(msgs.statusFail() + e.getLocalizedMessage());
        }
    }

    @Override
    public void onEnter() {
        super.onEnter();
        clearStatusLabel(msgs.selectFileMess());
    }

    // was at base class

    private static String handleException(SentimentUploadException caught) {
        String result = "<br/>";
        SentimentUploadException e = (SentimentUploadException) caught;
        String mess = SentimentUploadMessagesHelper.getMessage(e);
        result += (mess != null && !mess.isEmpty()) ? mess : e.getLocalizedMessage();
        return result;
    }

    // TODO: don't port

    private static final class SentimentUploadMessagesHelper {
	private static final Map<String, String> MAP = new HashMap<String, String>();
	
	static {
            MAP.put(SentimentUploadConstants.UPLOAD_ERROR_CANTSAVE, SentimentUploadMessages.INSTANCE.fileUploadErrorCantSave());
        }
        
        private SentimentUploadMessagesHelper() {
        }

	public static String getMessage(SentimentUploadException e) {
            StringBuilder result = new StringBuilder();
            String key = e.getErrorKey();

            result.append(getMessage(key));
            return result.toString();
	}

	public static String getMessage(String key) {
            String result = null;
            if (key != null) {
                result = MAP.get(key.toLowerCase());
            }
            return (result == null) ? "" : result;
	}
    }

    private static final class SentimentUploadConstants {
	public static final String UPLOAD_ERROR_CANTSAVE = "sentiment.upload.upload.error.cantsave";

	private SentimentUploadConstants() {
        }
    }
}
