package mygwt.web.client.sentiments.wizard.panels;

import mygwt.web.client.sentiments.upload.SentimentUploadException;
import mygwt.web.client.sentiments.upload.resources.SentimentUploadMessages;
import mygwt.web.client.sentiments.upload.resources.SentimentUploadMessagesHelper;
import mygwt.web.client.sentiments.wizard.ImportModel;
import mygwt.web.client.sentiments.wizard.steps.StepNavigator;
import mygwt.web.client.sentiments.wizard.panels.ButtonsPanel;

import mygwt.common.client.service.SentimentUploadServiceAsync;
import mygwt.common.client.widget.dialog.MessageDialog;

import mygwt.portal.dto.SentimentUploadValidationResult;
import mygwt.portal.dto.SentimentUploadConstants;

import mygwt.foundation.client.csrf.CsrfFormPanel;
import mygwt.foundation.client.csrf.ProjectIdAware;
import mygwt.foundation.client.rpc.AbstractAsyncCallback;
import mygwt.foundation.client.exception.ServiceException;
import mygwt.foundation.client.widget.AjaxLoaderImage;
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

public class ImportFinishPanel extends BasePanel {
    private static final String ERROR_MESSAGE_STYLE = "errorMessage";

    private SentimentUploadMessages msgs;

    private ImportModel importModel;
    private ButtonsPanel buttonsPanel;
    private StepNavigator stepNavigator;
    private SentimentUploadServiceAsync sentimentService;
/*
    private Image wheel;
    private HTML statusLabel;
    private FormPanel form;
    private Hidden projIdHidden;
    private FileUpload upload;

    private String sentFileName;
    private boolean waitingFileUploadValidationResults;
*/
    public ImportFinishPanel(ProjectIdAware projectIdAware, ImportModel importModel, ButtonsPanel buttonsPanel, StepNavigator stepNavigator, SentimentUploadServiceAsync sentimentService) {
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

    }
}
