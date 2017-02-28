package mygwt.web.client.sentiments.wizard;

import mygwt.common.client.url.Service;

import mygwt.common.client.service.SentimentImportService;
import mygwt.common.client.service.SentimentImportServiceAsync;
import mygwt.common.client.service.RecentSentimentExportsService;
import mygwt.common.client.service.RecentSentimentExportsServiceAsync;

import mygwt.foundation.client.rpc.AbstractAsyncCallback;
import mygwt.foundation.client.csrf.CsrfRpcRequestBuilder;

import mygwt.web.client.sentiments.resources.SentimentsMessages;

import mygwt.web.client.sentiments.wizard.steps.StepProvider;
import mygwt.web.client.sentiments.wizard.steps.StepNavigator;
import mygwt.web.client.sentiments.wizard.steps.NextPageDetector;

import mygwt.web.client.sentiments.wizard.panels.OperationSelectionPanel;
import mygwt.web.client.sentiments.wizard.panels.OperationSelectionPanel;
import mygwt.web.client.sentiments.wizard.panels.ImportFileSelectionPanel;
import mygwt.web.client.sentiments.wizard.panels.ImportFinishPanel;
import mygwt.web.client.sentiments.wizard.panels.CurrentExportPanel;
import mygwt.web.client.sentiments.wizard.panels.RecentExportsPanel;
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


public class SentimentsWizard extends BaseDialogBox implements StepProvider {
    private static final int STEPS_AVAILABLE_HEIGHT   = 450;
    private static final int BUTTONS_AVAILABLE_HEIGHT = 75;
    private static final int allH                     = STEPS_AVAILABLE_HEIGHT + BUTTONS_AVAILABLE_HEIGHT;
    private static final int allW                     = 670;

    private FinishHandler finishHandler;

    private SentimentsMessages msgs;

    private ImportModel importModel;

    private ProjectIdAwareImpl projectIdAwareImpl;

    private DockLayoutPanel dialogPanel;

    private DeckPanel stepsPanel;

    private StepNavigator stepNavigator;
    private OperationSelectionPanel stepOperationSelection;
    private CurrentExportPanel stepCurrentExport;
    private RecentExportsPanel stepRecentExports;
    private ImportFileSelectionPanel stepImportFileSelection;
    private ImportFinishPanel stepImportFinish;
    private ButtonsPanel buttonsPanel;

    private int currentStep = 0;

    public SentimentsWizard(FinishHandler finishHandler) {
        super("Sentiment Management", allW, allH);
        this.finishHandler = finishHandler;

        msgs = SentimentsMessages.INSTANCE;

        importModel = new ImportModel();

        setWidget(createDialogContents());

	setAutoHideEnabled(false);
    }

    private Widget createDialogContents() {
        projectIdAwareImpl = new ProjectIdAwareImpl();
        stepNavigator = new StepNavigator(this);
        buttonsPanel = new ButtonsPanel(stepNavigator);

        dialogPanel = new DockLayoutPanel(Unit.PX);
        dialogPanel.setSize(allW + "px", allH + "px");
        dialogPanel.setStyleName("AdHocWizardMainPanel");

        stepsPanel = new DeckPanel();
	stepsPanel.setSize(allW + "px", STEPS_AVAILABLE_HEIGHT + "px");

        stepOperationSelection = new OperationSelectionPanel();
        stepImportFileSelection = new ImportFileSelectionPanel(projectIdAwareImpl, importModel, buttonsPanel, stepNavigator, getSentimentImportSvcAsync());
        stepImportFinish = new ImportFinishPanel(projectIdAwareImpl, finishHandler, importModel, buttonsPanel, stepNavigator, getSentimentImportSvcAsync());
        stepCurrentExport = new CurrentExportPanel(projectIdAwareImpl);
        stepRecentExports = new RecentExportsPanel(projectIdAwareImpl, finishHandler, getRecentSentimentExportsSvcAsync());

        configureWizard();

        return dialogPanel;
    }

    private void configureWizard() {
        dialogPanel.clear();

        stepsPanel.add(stepNavigator.addPage(stepOperationSelection));  
        stepsPanel.add(stepNavigator.addPage(stepImportFileSelection));  
        stepsPanel.add(stepNavigator.addPage(stepImportFinish));  
        stepsPanel.add(stepNavigator.addPage(stepCurrentExport));  
        stepsPanel.add(stepNavigator.addPage(stepRecentExports));  
        
        stepNavigator.addNextPageDetector(stepOperationSelection,
            new NextPageDetector() {
                @Override
                public WizardPage next() {
                    if (stepOperationSelection.isImportSelected()) {
                        return stepImportFileSelection;
                    } else if (stepOperationSelection.isExportCurSelected()) {
                        return stepCurrentExport;
                    } else if (stepOperationSelection.isExportPrevSelected()) {
                        return stepRecentExports;
                    }
                    return null; // impossible
                }
            }
        );
        stepNavigator.addNextPageTo(stepImportFileSelection, stepImportFinish);
        
        dialogPanel.addNorth(stepsPanel, STEPS_AVAILABLE_HEIGHT);

        buttonsPanel.setSize(allW + "px", BUTTONS_AVAILABLE_HEIGHT + "px");

        buttonsPanel.disableBack();
        buttonsPanel.enableNext();
        buttonsPanel.disableFinish();

        dialogPanel.addSouth(buttonsPanel, BUTTONS_AVAILABLE_HEIGHT);

        stepNavigator.start(stepOperationSelection);
    }

    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
    }

    public ImportModel getImportModel() {
        return importModel;
    }

    public long getProjectId() {
        return projectIdAwareImpl.getProjectId();
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

    @Override
    public void showStep(int stepIdx, boolean isFirst, boolean isLast, String traceMsg) {
        stepsPanel.showWidget(stepIdx);
        WizardPage currentPage = stepNavigator.getCurrentPage();
        buttonsPanel.onPageChanged(currentPage, isFirst, isLast);
    }

    private RecentSentimentExportsServiceAsync svcRecentSentimentExportsAsync;

    public RecentSentimentExportsServiceAsync getRecentSentimentExportsSvcAsync() {
        if (svcRecentSentimentExportsAsync == null) {
            svcRecentSentimentExportsAsync = (RecentSentimentExportsServiceAsync) GWT.create(RecentSentimentExportsService.class);
            injectRpcBuilder(((ServiceDefTarget) svcRecentSentimentExportsAsync), Service.SENTIMENT_TRANSFER_SERVICE);
        }
        return svcRecentSentimentExportsAsync;
    }

    private void injectRpcBuilder(final ServiceDefTarget target, final Service service) {
        final String absUrl = service.getAbsoluteUrl();
        // we typically use this instead of the @com.google.gwt.user.client.rpc.RemoteServiceRelativePath
        target.setServiceEntryPoint(absUrl); 
        target.setRpcRequestBuilder(CsrfRpcRequestBuilder.getInstance(projectIdAwareImpl));
    }

    private SentimentImportServiceAsync svcSentimentImportServiceAsync;
    
    public SentimentImportServiceAsync getSentimentImportSvcAsync() {
        if (svcSentimentImportServiceAsync == null) {
            svcSentimentImportServiceAsync = (SentimentImportServiceAsync) GWT.create(SentimentImportService.class);
            injectRpcBuilder(((ServiceDefTarget) svcSentimentImportServiceAsync), Service.SENTIMENT_TRANSFER_SERVICE);
        }
        return svcSentimentImportServiceAsync;
    }

    private void clearSession() {
        getSentimentImportSvcAsync().cleanupSentimentsWithUploadedData(getProjectId(), AbstractAsyncCallback.VOID_CALLBACK);
    }
}
