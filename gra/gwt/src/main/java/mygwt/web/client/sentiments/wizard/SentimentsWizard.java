package mygwt.web.client.sentiments.wizard;

import mygwt.common.client.url.Service;

import mygwt.common.client.service.SentimentUploadServiceAsync;
import mygwt.common.client.service.SentimentUploadService;

import mygwt.portal.dto.SentimentUploadValidationResult;

import mygwt.foundation.client.rpc.AbstractAsyncCallback;

import mygwt.foundation.client.csrf.ProjectIdAware;
import mygwt.foundation.client.csrf.CsrfRpcRequestBuilder;

import mygwt.web.client.sentiments.rse.RecentSentimentExportsService;
import mygwt.web.client.sentiments.rse.RecentSentimentExportsServiceAsync;

import mygwt.web.client.sentiments.resources.SentimentsMessages;

import mygwt.web.client.sentiments.wizard.steps.StepProvider;
import mygwt.web.client.sentiments.wizard.steps.StepNavigator;
import mygwt.web.client.sentiments.wizard.steps.NextPageDetector;

import mygwt.web.client.sentiments.wizard.panels.OperationSelectionPanel;
import mygwt.web.client.sentiments.wizard.panels.OperationSelectionPanel;
import mygwt.web.client.sentiments.wizard.panels.SentimentImportFileSelectionPanel;
import mygwt.web.client.sentiments.wizard.panels.SentimentExportPanel;
import mygwt.web.client.sentiments.wizard.panels.RecentSentimentExportsPanel;
import mygwt.web.client.sentiments.wizard.panels.ButtonsPanel;

import mygwt.web.client.utils.LogUtils;

import mygwt.foundation.client.widget.dialog.BaseDialogBox;
import mygwt.foundation.client.widget.button.CancelButton;
import mygwt.foundation.client.widget.button.OkButton;
import mygwt.foundation.client.widget.list.GroupedListBox;

import com.google.gwt.core.client.GWT;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.user.client.Event;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import com.google.gwt.user.client.rpc.ServiceDefTarget;


public class SentimentsWizard extends BaseDialogBox implements StepProvider, ProjectIdAware {
    private static final int STEPS_AVAILABLE_HEIGHT   = 450;
    private static final int BUTTONS_AVAILABLE_HEIGHT = 75;
    private static final int allH                     = STEPS_AVAILABLE_HEIGHT + BUTTONS_AVAILABLE_HEIGHT;
    private static final int allW                     = 670;

    public static class ImportModel {
        private SentimentUploadValidationResult sentimentUploadValidationResult;

        public SentimentUploadValidationResult getSentimentUploadValidationResult() {
            return sentimentUploadValidationResult;
        }

        public void setSentimentUploadValidationResult(SentimentUploadValidationResult sentimentUploadValidationResult) {
            this.sentimentUploadValidationResult = sentimentUploadValidationResult;
        } 
    }

    private SentimentsMessages msgs;

    private ImportModel importModel;

    private long projectId;

    private DockLayoutPanel dialogPanel;

    private DeckPanel stepsPanel;

    private StepNavigator stepNavigator;
    private OperationSelectionPanel stepOperationSelection;
    private SentimentExportPanel stepSentimentExport;
    private RecentSentimentExportsPanel stepResentSentimentExports;
    private SentimentImportFileSelectionPanel stepSentimentImportFileSelection;
    private ButtonsPanel buttonsPanel;

    private int currentStep = 0;

    public SentimentsWizard() {
        super("Sentiment Management", allW, allH); // SentimentsMessages.INSTANCE.rseTitle() change title !!!

        msgs = SentimentsMessages.INSTANCE;

        importModel = new ImportModel();

        //hack for IE6/7(CMP-15701)
        //DeckPanel hackPanel = new DeckPanel();
        //hackPanel.setHeight(STEPS_AVAILABLE_HEIGHT + "px");
        //setWidget(hackPanel);

        setWidget(createDialogContents());

	setAutoHideEnabled(false);
    }

    private Widget createDialogContents() {
        stepNavigator = new StepNavigator(this);
        buttonsPanel = new ButtonsPanel(stepNavigator);

        dialogPanel = new DockLayoutPanel(Unit.PX);
        dialogPanel.setSize(allW + "px", allH + "px");
        dialogPanel.setStyleName("AdHocWizardMainPanel");

        stepsPanel = new DeckPanel();
	stepsPanel.setSize(allW + "px", STEPS_AVAILABLE_HEIGHT + "px");
        //stepsPanel.addStyleName("myDeckPanel");

        stepOperationSelection = new OperationSelectionPanel();
        stepSentimentImportFileSelection = new SentimentImportFileSelectionPanel(this, buttonsPanel, getSentimentUploadSvcAsync());
        stepSentimentExport = new SentimentExportPanel();
        stepResentSentimentExports = new RecentSentimentExportsPanel(getRecentSentimentExportsSvcAsync());
        // ...(this);

        configureWizard();

        return dialogPanel;
    }

    private void configureWizard() {
        dialogPanel.clear();

        stepsPanel.add(stepNavigator.addPage(stepOperationSelection));  
        stepsPanel.add(stepNavigator.addPage(stepSentimentImportFileSelection));  
        stepsPanel.add(stepNavigator.addPage(stepSentimentExport));  
        stepsPanel.add(stepNavigator.addPage(stepResentSentimentExports));  
        
        stepNavigator.addNextPageDetector(stepOperationSelection,
            new NextPageDetector() {
                @Override
                public WizardPage next() {
                    if (stepOperationSelection.isImportSelected()) {
                        return stepSentimentImportFileSelection;
                    } else if (stepOperationSelection.isExportCurSelected()) {
                        return stepSentimentExport;
                    } else if (stepOperationSelection.isExportPrevSelected()) {
                        return stepResentSentimentExports;
                    }
                    return null; // impossible
                }
            }
        );
        
        dialogPanel.addNorth(stepsPanel, STEPS_AVAILABLE_HEIGHT);

        buttonsPanel.setSize(allW + "px", BUTTONS_AVAILABLE_HEIGHT + "px");

        buttonsPanel.disableBack();
        buttonsPanel.enableNext();
        buttonsPanel.disableFinish();

        // ??? dialogPanel.add(placeHolder);
        dialogPanel.addSouth(buttonsPanel, BUTTONS_AVAILABLE_HEIGHT);

        stepNavigator.start(stepOperationSelection);
        //stepNavigator.start(stepResentSentimentExports);
    }

    @Override
    protected void onOpen() {
        super.onOpen();
        WizardPage currentPage = stepNavigator.getCurrentPage();
        if (currentPage != null) {
            currentPage.onEnter();
        }	
    }

    @Override
    protected void onClose() {	
        super.onClose();
    }

    //@Override
    public void onCancel() {
        clearSession();
        this.hide();
    }

    public void showUploadResults() {
        WizardPage currentPage = stepNavigator.getCurrentPage();
        if (currentPage == stepSentimentImportFileSelection) {
            stepNavigator.onNext();
        }
    }

    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
    }

    public ImportModel getImportModel() {
            return importModel;
    }

    // ApplicationContext.get().getProjectId();

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    @Override
    public long getProjectId() {
        return projectId;
    }

    @Override
    public void showStep(int stepIdx, boolean isFirst, boolean isLast, String traceMsg) {
        stepsPanel.showWidget(stepIdx);
        WizardPage currentPage = stepNavigator.getCurrentPage();
        buttonsPanel.onPageChanged(currentPage, isFirst, isLast);
        //LogUtils.log(traceMsg);
    }

    private RecentSentimentExportsServiceAsync svcRecentSentimentExportsAsync;

    public RecentSentimentExportsServiceAsync getRecentSentimentExportsSvcAsync() {
        if (svcRecentSentimentExportsAsync == null) {
            svcRecentSentimentExportsAsync = (RecentSentimentExportsServiceAsync) GWT.create(RecentSentimentExportsService.class);
            injectRpcBuilder(((ServiceDefTarget) svcRecentSentimentExportsAsync), Service.RECENT_SENTIMENT_EXPORTS_SERVICE);
        }
        return svcRecentSentimentExportsAsync;
    }

    private void injectRpcBuilder(final ServiceDefTarget target, final Service service) {
        final String absUrl = service.getAbsoluteUrl();
        // we typically use this instead of the @com.google.gwt.user.client.rpc.RemoteServiceRelativePath
        target.setServiceEntryPoint(absUrl); 
        target.setRpcRequestBuilder(CsrfRpcRequestBuilder.getInstance(this));
    }

    private SentimentUploadServiceAsync svcSentimentUploadServiceAsync;
    
    public SentimentUploadServiceAsync getSentimentUploadSvcAsync() {
        if (svcSentimentUploadServiceAsync == null) {
            svcSentimentUploadServiceAsync = (SentimentUploadServiceAsync) GWT.create(SentimentUploadService.class);
            injectRpcBuilder(((ServiceDefTarget) svcSentimentUploadServiceAsync), Service.SENTIMENT_UPLOAD_SERVICE);
        }
        return svcSentimentUploadServiceAsync;
    }

    private void clearSession() {
        getSentimentUploadSvcAsync().cleanupSentimentsWithUploadedData(getProjectId(), AbstractAsyncCallback.VOID_CALLBACK);
    }
}
