package mygwt.web.client.sentiments.wizard;

import mygwt.common.client.url.Service;

import mygwt.foundation.client.csrf.ProjectIdAware;
import mygwt.foundation.client.csrf.CsrfRpcRequestBuilder;

import mygwt.web.client.sentiments.rse.RecentSentimentExportsService;
import mygwt.web.client.sentiments.rse.RecentSentimentExportsServiceAsync;

import mygwt.web.client.sentiments.resources.SentimentsMessages;

import mygwt.web.client.sentiments.wizard.steps.StepProvider;
import mygwt.web.client.sentiments.wizard.steps.StepNavigator;
import mygwt.web.client.sentiments.wizard.steps.NextPageDetector;

import mygwt.web.client.sentiments.wizard.panels.OperationSelectionPanel;
import mygwt.web.client.sentiments.wizard.panels.RecentSentimentExportsPanel;
import mygwt.web.client.sentiments.wizard.panels.TempPanel;
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

    private SentimentsMessages msgs;

    private final long projectId;

    private DockLayoutPanel dialogPanel;

    private DeckPanel steps;

    private StepNavigator stepNavigator;
    private OperationSelectionPanel stepOperationSelection;
    private RecentSentimentExportsPanel stepResentSentimentExports;
    private TempPanel step2;
    private TempPanel step3;
    private TempPanel step4;
    private ButtonsPanel buttonsPanel;

    private int currentStep = 0;

    public SentimentsWizard(long projectId) {
        super("Sentiment Management", allW, allH); // SentimentsMessages.INSTANCE.rseTitle() change title !!!
        this.projectId = projectId;

        msgs = SentimentsMessages.INSTANCE;

        //hack for IE6/7(CMP-15701)
        //DeckPanel hackPanel = new DeckPanel();
        //hackPanel.setHeight(STEPS_AVAILABLE_HEIGHT + "px");
        //setWidget(hackPanel);

        setWidget(createDialogContents());

	setAutoHideEnabled(false);
    }

    private Widget createDialogContents() {
        dialogPanel = new DockLayoutPanel(Unit.PX);
        dialogPanel.setSize(allW + "px", allH + "px");
        //dialogPanel.setStyleName("AdHocWizardMainPanel"); // TODO: put back

        steps = new DeckPanel();
	steps.setSize(allW + "px", STEPS_AVAILABLE_HEIGHT + "px");
        //steps.addStyleName("myDeckPanel");

        stepOperationSelection = new OperationSelectionPanel();
        step2 = new TempPanel("here will be import");
        step3 = new TempPanel("here will be export current");
        stepResentSentimentExports = new RecentSentimentExportsPanel(projectId, getSvcAsync());
        // ...(this);

        stepNavigator = new StepNavigator(this);
        buttonsPanel = new ButtonsPanel(stepNavigator);
        buttonsPanel.setSize(allW + "px", BUTTONS_AVAILABLE_HEIGHT + "px");

        configureWizard();

        return dialogPanel;
    }

    private void configureWizard() {
        dialogPanel.clear();

        steps.add(stepNavigator.addPage(stepOperationSelection));  
        steps.add(stepNavigator.addPage(step2));  
        steps.add(stepNavigator.addPage(step3));  
        steps.add(stepNavigator.addPage(stepResentSentimentExports));  
        
        stepNavigator.addNextPageDetector(stepOperationSelection,
            new NextPageDetector() {
                @Override
                public WizardPage next() {
                    if (stepOperationSelection.isImportSelected()) {
                        return step2;
                    } else if (stepOperationSelection.isExportCurSelected()) {
                        return step3;
                    } else if (stepOperationSelection.isExportPrevSelected()) {
                        return stepResentSentimentExports;
                    }
                    return null; // impossible
                }
            }
        );
        
        dialogPanel.addNorth(steps, STEPS_AVAILABLE_HEIGHT);

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

    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
    }

    @Override
    public long getProjectId() {
        return projectId;
    }

    @Override
    public void showStep(int stepIdx, boolean isFirst, boolean isLast, String traceMsg) {
        steps.showWidget(stepIdx);
        WizardPage currentPage = stepNavigator.getCurrentPage();
        buttonsPanel.onPageChanged(currentPage, isFirst, isLast);
        //LogUtils.log(traceMsg);
    }

    private RecentSentimentExportsServiceAsync svcAsync;

    public RecentSentimentExportsServiceAsync getSvcAsync() {
        if (svcAsync == null) {
            svcAsync = (RecentSentimentExportsServiceAsync) GWT.create(RecentSentimentExportsService.class);
            injectRpcBuilder(((ServiceDefTarget) svcAsync), Service.RECENT_SENTIMENT_EXPORTS_SERVICE);
        }
        return svcAsync;
    }

    private void injectRpcBuilder(final ServiceDefTarget target, final Service service) {
        final String absUrl = service.getAbsoluteUrl();
        // we typically use this instead of the @com.google.gwt.user.client.rpc.RemoteServiceRelativePath
        target.setServiceEntryPoint(absUrl); 
        target.setRpcRequestBuilder(CsrfRpcRequestBuilder.getInstance(this));
    }
}
