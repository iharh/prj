package mygwt.web.client.sentiments.wizard.panels;

import mygwt.web.client.sentiments.upload.SentimentUploadException;
import mygwt.web.client.sentiments.upload.resources.SentimentUploadMessages;
import mygwt.web.client.sentiments.upload.resources.SentimentUploadMessagesHelper;
import mygwt.web.client.sentiments.wizard.ImportModel;
import mygwt.web.client.sentiments.wizard.steps.StepNavigator;
import mygwt.web.client.sentiments.wizard.panels.ButtonsPanel;

import mygwt.common.client.service.SentimentImportServiceAsync;
import mygwt.common.client.widget.dialog.MessageDialog;

import mygwt.portal.dto.SentimentUploadValidationResult;
import mygwt.portal.dto.SentimentUploadConstants;

import mygwt.foundation.client.csrf.CsrfFormPanel;
import mygwt.foundation.client.csrf.ProjectIdAware;
import mygwt.foundation.client.rpc.AbstractAsyncCallback;
import mygwt.foundation.client.exception.ServiceException;
import mygwt.foundation.client.resources.CommonConstants;
//import mygwt.foundation.client.widget.AjaxLoaderImage;
import mygwt.foundation.client.widget.dialog.BaseDialogBox;
import mygwt.foundation.client.widget.dialog.SessionExpiredDialog;
import mygwt.foundation.client.widget.dialog.YesNoDialog;

import mygwt.web.client.utils.LogUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Hidden;
//import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;
import com.google.gwt.user.client.ui.Widget;

public class ImportFinishPanel extends BasePanel {
    private static final String ERROR_MESSAGE_STYLE = "errorMessage";
    private static final String EMPTY_ANCHOR = "javascript:;";

    private SentimentUploadMessages msgs;

    private ImportModel importModel;
    private ButtonsPanel buttonsPanel;
    private StepNavigator stepNavigator;
    private SentimentImportServiceAsync sentimentService;

    private Anchor showSkippedWordsBtn;
    private Anchor showSkippedRulesBtn;

    // final
    //private Image stgLoadImage;
    private HTML statusLabel; // final
    private HTML wordsLabel;
    private HTML rulesLabel;

    private CheckBox launchSentenceExceptionsUpdate;

    private boolean isProcessStarted;

    public ImportFinishPanel(ProjectIdAware projectIdAware, ImportModel importModel, ButtonsPanel buttonsPanel, StepNavigator stepNavigator, SentimentImportServiceAsync sentimentService) {
	super(projectIdAware); // SentimentUploadMessages.INSTANCE.step2of2()

        this.importModel = importModel;
        this.buttonsPanel = buttonsPanel;
        this.stepNavigator = stepNavigator;
        this.sentimentService = sentimentService;

        msgs = SentimentUploadMessages.INSTANCE;

        createMainContent();
    }

    private void createMainContent() {
        add(new HTML(msgs.warningHtml()));

        HTML please = new HTML(msgs.uploadDetails());
        add(please);
        setCellHeight(please, "7%");

	showSkippedWordsBtn = new Anchor(CommonConstants.EMPTY_STRING, EMPTY_ANCHOR);
	showSkippedWordsBtn.addClickHandler(new ShowSkippedWordsListener());
        showSkippedWordsBtn.setVisible(false);

	showSkippedRulesBtn = new Anchor(CommonConstants.EMPTY_STRING, EMPTY_ANCHOR);
	showSkippedRulesBtn.addClickHandler(new ShowSkippedRulesListener());
        showSkippedRulesBtn.setVisible(false);

	//stgLoadImage = new AjaxLoaderImage();
        //stgLoadImage.setVisible(false);

        statusLabel = new HTML();
        statusLabel.setVisible(false);

        VerticalPanel infoPanel = new VerticalPanel();
        infoPanel.setWidth("100%");

        VerticalPanel stgLoadResultPanel = new VerticalPanel();
        stgLoadResultPanel.setSpacing(2);
        stgLoadResultPanel.add(statusLabel);

        HorizontalPanel wordsPanel = new HorizontalPanel();
        wordsPanel.setSpacing(5);

        wordsLabel = new HTML();
        wordsPanel.add(wordsLabel);
        wordsPanel.add(showSkippedWordsBtn);
        stgLoadResultPanel.add(wordsPanel);

        HorizontalPanel rulesPanel = new HorizontalPanel();
        rulesPanel.setSpacing(5);

        rulesLabel = new HTML();
        rulesPanel.add(rulesLabel);
        rulesPanel.add(showSkippedRulesBtn);
        stgLoadResultPanel.add(rulesPanel);

        infoPanel.add(stgLoadResultPanel);

	VerticalPanel launchModelSelectionPanel = new VerticalPanel();
        launchModelSelectionPanel.setSpacing(10);

        launchSentenceExceptionsUpdate = new CheckBox(msgs.updateSentiments());
        launchSentenceExceptionsUpdate.setChecked(true);
        launchModelSelectionPanel.add(launchSentenceExceptionsUpdate);		
        infoPanel.insert(launchModelSelectionPanel, 0);

        add(infoPanel);
    }

    private void clearStatusLabel() {
        clearStatusLabel(CommonConstants.EMPTY_STRING);
    }

    private void clearStatusLabel(String text) {
        statusLabel.removeStyleName(ERROR_MESSAGE_STYLE);
        statusLabel.setHTML(text);
    }

    private class ShowSkippedWordsListener implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            LogUtils.log("onClick ShowSkippedWordsListener");
            //stgLoadImage.setVisible(true);
            BaseDialogBox skippedBox = new BaseDialogBox(msgs.skippedWordsRows(), 410, 150) {
                {
                    setWidget(createDialogContents());
                }
                protected Widget createDialogContents() {
                    VerticalPanel outerPanel = new VerticalPanel();
                    outerPanel.setSize("400px", "150px");
                    outerPanel.setSpacing(4);
                    VerticalPanel content = new VerticalPanel();
                    content.setSize("400px", "150px");
                    SkippedRowPanel skippedRowPanel = new SkippedRowPanel(msgs.wordTitle());
                    skippedRowPanel.fillData(importModel.getSentimentUploadValidationResult().getSkippedWordRows());
                    skippedRowPanel.setVisible(true);
                    content.add(skippedRowPanel);
                    outerPanel.add(content);
                    return outerPanel;
                }
            };
            skippedBox.setModal(true);
            skippedBox.show();
        }
    }

    private class ShowSkippedRulesListener implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            LogUtils.log("onClick ShowSkippedRulesListener");
            //stgLoadImage.setVisible(true);
            BaseDialogBox skippedBox = new BaseDialogBox(msgs.skippedRulesRows(), 400, 150) {
                {
                    setWidget(createDialogContents());
                }
                protected Widget createDialogContents() {
                    VerticalPanel outerPanel = new VerticalPanel();
                    outerPanel.setSize("400px", "150px");
                    outerPanel.setSpacing(4);
                    VerticalPanel content = new VerticalPanel();
                    content.setSize("400px", "150px");
                    SkippedRowPanel skippedRowPanel = new SkippedRowPanel(msgs.ruleTitle());
                    skippedRowPanel.fillData(importModel.getSentimentUploadValidationResult().getSkippedRulesRows());
                    skippedRowPanel.setVisible(true);
                    content.add(skippedRowPanel);
                    outerPanel.add(content);
                    return outerPanel;
                }
            };
            //stgLoadImage.setVisible(false);
            skippedBox.setModal(true);
            skippedBox.show();
        }
    }

    @Override
    public void onEnter() {
        super.onEnter();
        clearStatusLabel();
        showSkippedWordsBtn.setVisible(false);
        showSkippedRulesBtn.setVisible(false);
        wordsLabel.setHTML(CommonConstants.EMPTY_STRING);
        rulesLabel.setHTML(CommonConstants.EMPTY_STRING);

        SentimentUploadValidationResult result = importModel.getSentimentUploadValidationResult();
        if (result == null) {
            statusLabel.setVisible(true);
            statusLabel.addStyleName(ERROR_MESSAGE_STYLE);
            statusLabel.setText(msgs.noDataInFile());
        } else {
            wordsLabel.setText(msgs.importedWords(result.getWordsImported()));
            int skippedWords = result.getSkippedWordRows().size();
            if (skippedWords > 0) {
                showSkippedWordsBtn.setText(skippedWords == 1 ? msgs.skippedWord() : msgs.skippedWords(skippedWords));
                showSkippedWordsBtn.setVisible(true);
            }
            rulesLabel.setText(msgs.importedRules(result.getRulesImported()));
            int skippedRules = result.getSkippedRulesRows().size();
            if (skippedRules > 0) {
                showSkippedRulesBtn.setText(skippedRules == 1 ? msgs.skippedRule() : msgs.skippedRules(skippedRules));
                showSkippedRulesBtn.setVisible(true);
            }
        }
    }

    @Override
    public void onFinish() {
        if (isProcessStarted) {
            statusLabel.addStyleName(ERROR_MESSAGE_STYLE);
            statusLabel.setVisible(false);
            statusLabel.setVisible(true);
            statusLabel.removeStyleName(ERROR_MESSAGE_STYLE);
        } else {
            isProcessStarted = true;
            buttonsPanel.disableFinish();
            clearStatusLabel(msgs.processingData());
            try {
                sentimentService.updateSentimentsWithUploadedData(getProjectId(),
                    new AbstractAsyncCallback<Void>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            LogUtils.log("onF");
                            String errorMessage = msgs.errorOnDBSynchronizing();
                            // stgLoadImage.setVisible(false);
                            if (caught instanceof SentimentUploadException) {
                                errorMessage += handleException((SentimentUploadException) caught);
                            } else {
                                LogUtils.log("other ex: " + errorMessage);
                                //super.onFailure(caught);
                            }
                            statusLabel.setVisible(true);
                            statusLabel.addStyleName(ERROR_MESSAGE_STYLE);
                            statusLabel.setHTML(errorMessage);
                            isProcessStarted = false;
                            buttonsPanel.enableFinish();

                            GWT.log("Failed to synchronize database with the uploaded file", caught); //$NON-NLS-1$
                        }
                        @Override
                        public void onSuccess(Void result) {
                            LogUtils.log("onS");
                            clearStatusLabel(msgs.done());
                            isProcessStarted = false;
                            buttonsPanel.enableFinish();
                            Boolean updateSentences = launchSentenceExceptionsUpdate.getValue();
                            // getWizard().finishComplete(updateSentences); // TODO: TBD
                        }
                    }
                );
            } catch (ServiceException e) {
                LogUtils.log("3");
                statusLabel.addStyleName(ERROR_MESSAGE_STYLE);
                statusLabel.setHTML(e.getLocalizedMessage());
            }
        }
    }
}
