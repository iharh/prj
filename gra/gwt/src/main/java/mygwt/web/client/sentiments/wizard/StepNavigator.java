package mygwt.web.client.sentiments.wizard;

import mygwt.web.adhoc.client.wizard.WizardPage;
import mygwt.web.adhoc.client.wizard.WizardActionHandler;

import mygwt.foundation.client.widget.dialog.BaseDialogBox;

import mygwt.web.client.sentiments.wizard.panels.ButtonsPanel;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class StepNavigator implements WizardActionHandler {
    BaseDialogBox parent;
    ButtonsPanel buttonsPanel;

    private DeckPanel steps;
    private int stepsSize;
    private Map<WizardPage, Integer> stepIndices;

    private WizardPage currentPage;
    private int currentStep;

    private LinkedList<WizardPage> deque;

    public StepNavigator(BaseDialogBox parent, ButtonsPanel buttonsPanel, DeckPanel steps) {
        this.parent = parent;
        this.buttonsPanel = buttonsPanel;
        this.steps = steps;

        deque = new LinkedList<WizardPage>();
        stepIndices = new HashMap<WizardPage, Integer>();
    }

    public<T extends WizardPage> T add(T panel) {
        // steps.add(panel);
        deque.add(panel);
        stepIndices.put(panel, stepsSize);
        ++stepsSize;
        return panel;
    }

    public void m1() {
        currentPage = deque.peekFirst();
        currentStep = 0;
    }

    private void showStep(int idx) {
        steps.showWidget(currentStep);
    }

    @Override
    public void onNext() {
        currentPage.onLeave();
        ++currentStep;
        currentPage = (WizardPage) steps.getWidget(currentStep);
        showStep(currentStep);

        buttonsPanel.onPageChanged(currentPage, false, currentStep == stepsSize); // TODO: use other way to check this
        currentPage.onEnter();
    }

    @Override
    public void onBack() {
        currentPage.onLeave();
        currentPage = deque.pop();

        currentStep = stepIndices.get(currentPage);
        showStep(currentStep);

        buttonsPanel.onPageChanged(currentPage, stepIndices.isEmpty(), false); // , isFirst, isLast
        currentPage.onEnter();
    }

    @Override
    public void onCancel() {		
        parent.hide();
    }

    @Override
    public void onFinish() {
        currentPage.onFinish();		
    }

    @Override
    public WizardPage getCurrentPage() {
        return currentPage;
    }

    /*@Override
    protected void onOpen() {
        parent.onOpen();
        WizardPage currentPage = ...getCurrentPage();
        if (currentPage != null) {
            currentPage.onEnter();
        }	
    }*/
}
