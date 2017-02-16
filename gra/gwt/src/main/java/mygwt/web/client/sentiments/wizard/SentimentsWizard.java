package mygwt.web.client.sentiments.wizard;

import mygwt.foundation.client.csrf.ProjectIdAware;

import mygwt.web.client.sentiments.wizard.panels.OperationSelectionPanel;
import mygwt.web.client.sentiments.wizard.panels.ButtonsPanel;

import mygwt.foundation.client.widget.dialog.BaseDialogBox;
import mygwt.foundation.client.widget.button.CancelButton;
import mygwt.foundation.client.widget.button.OkButton;
import mygwt.foundation.client.widget.list.GroupedListBox;

import mygwt.web.adhoc.client.wizard.WizardPage;
import mygwt.web.adhoc.client.wizard.WizardActionHandler;

import mygwt.web.client.sentiments.resources.SentimentsMessages;

import com.google.gwt.core.client.GWT;

import com.google.gwt.dom.client.Style.Unit;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.user.client.Event;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Panel;
// http://www.gwtproject.org/javadoc/latest/com/google/gwt/user/client/ui/DeckPanel.html
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


public class SentimentsWizard extends BaseDialogBox implements WizardActionHandler, ProjectIdAware {
    private static final int STEPS_USUAL_HEIGHT = 250;
    private static final int INFO_USUAL_HEIGHT = 200;	
    private static final int STEPS_AVAILABLE_HEIGHT = INFO_USUAL_HEIGHT + STEPS_USUAL_HEIGHT;

    private static final int borderW     = 0; // 1 for borders vis-n
    private static final int allW        = 670;
    private static final int allH        = 525;
    private static final int spacing     = 12; // 10

    private SentimentsMessages msgs;

    private final long projectId;

    private VerticalPanel dialogVPanel;

    private DeckPanel steps;

    private OperationSelectionPanel step1;
    private ButtonsPanel buttonsPanel;

    private WizardPage currentPage;
    private int currentStep = 0;

    public SentimentsWizard(long projectId) {
        super("Sentiment Management", allW, allH); // SentimentsMessages.INSTANCE.rceTitle() change title !!!
        this.projectId = projectId;

        msgs = SentimentsMessages.INSTANCE;

        //hack for IE6/7(CMP-15701)
        //DeckPanel hackPanel = new DeckPanel();
        //hackPanel.setHeight("450px");
        //setWidget(hackPanel);

        setWidget(createDialogContents());

	setAutoHideEnabled(false);
        //hide();
    }

    private Widget createDialogContents() {
        /*
        DockLayoutPanel panel = new DockLayoutPanel(Unit.PX);

        panel.setWidth(allW + "px");
        panel.setHeight(allH + "px");

        panel.addNorth(createMainPanel(), 250);
        panel.addSouth(createButtonPanel(), 50);

        return panel;
        */
        dialogVPanel = new VerticalPanel();
        dialogVPanel.setStyleName("AdHocWizardMainPanel");
        dialogVPanel.setSpacing(spacing);
        dialogVPanel.setSize("100%", "100%");

        steps = new DeckPanel();
	steps.setHeight(Integer.toString(STEPS_AVAILABLE_HEIGHT) + "px");

        step1 = new OperationSelectionPanel();
        // ...(this);

        buttonsPanel = new ButtonsPanel(this);

        configureWizard();

        return dialogVPanel;
    }

    private void configureWizard() {
        dialogVPanel.clear();

        steps.add(step1);  // index 0
        // steps.remove(int idx)
        // !!! we can add steps in any order and just need to calculate prev/next indices in appropriate way
        
        currentPage = step1;
        steps.showWidget(currentStep);
        
        dialogVPanel.add(steps);

        buttonsPanel.disableBack();
        buttonsPanel.enableNext();
        buttonsPanel.disableFinish();

        dialogVPanel.add(buttonsPanel);
        // ??? dialogVPanel.add(placeHolder);
    }

    @Override
    protected void onOpen() {
        super.onOpen();
        if (currentPage != null) {
            currentPage.onEnter();
        }	
    }

    @Override
    public void onBack() {
        currentPage.onLeave();
        --currentStep;
        currentPage = (WizardPage) steps.getWidget(currentStep);
        steps.showWidget(currentStep);

        buttonsPanel.onPageChanged(currentPage, currentStep == 0, false); // , isFirst, isLast
        currentPage.onEnter();
    }

    @Override
    public void onCancel() {		
        this.hide();
    }

    @Override
    public void onFinish() {
        currentPage.onFinish();		
    }

    @Override
    public void onNext() {
        currentPage.onLeave();
        ++currentStep;
        currentPage = (WizardPage) steps.getWidget(currentStep);
        steps.showWidget(currentStep);

        buttonsPanel.onPageChanged(currentPage, false, currentStep == 1); // TODO: fix number
        currentPage.onEnter();
    }
    
    @Override
    protected void onClose() {	
        super.onClose();
    }

    @Override
    public WizardPage getCurrentPage() {
        return currentPage;
    }

    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
    }

    @Override
    public long getProjectId() {
        return projectId;
    }
}
