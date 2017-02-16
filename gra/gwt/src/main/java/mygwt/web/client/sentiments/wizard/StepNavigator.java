package mygwt.web.client.sentiments.wizard;

import mygwt.web.adhoc.client.wizard.WizardPage;
import mygwt.web.adhoc.client.wizard.WizardActionHandler;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

public class StepNavigator implements WizardActionHandler {
    StepProvider stepProvider;

    private DeckPanel steps;
    private int stepsSize;
    private Map<WizardPage, Integer> stepIndices;

    private WizardPage currentPage;
    private int currentStep;

    private LinkedList<WizardPage> deque;

    public StepNavigator(StepProvider stepProvider, DeckPanel steps) {
        this.stepProvider = stepProvider;
        this.steps = steps;

        deque = new LinkedList<WizardPage>();
        stepIndices = new HashMap<WizardPage, Integer>();
    }

    public<T extends WizardPage> T add(T panel) {
        deque.add(panel);
        stepIndices.put(panel, stepsSize);
        ++stepsSize;
        return panel;
    }

    public void doneAdding() {
        currentPage = deque.getFirst();
        currentStep = 0;
        stepProvider.showStep(currentStep, true, false, "doneAdding"); // TODO: use other way to check this
    }

    @Override
    public void onNext() {
        currentPage.onLeave();
        deque.add(currentPage);
        ++currentStep;

        currentPage = (WizardPage) steps.getWidget(currentStep);

        stepProvider.showStep(currentStep, false, currentStep == stepsSize, // TODO: use other way to check this
            "onNext: " + currentStep + "deque size: " + deque.size());
        currentPage.onEnter();
    }

    @Override
    public void onBack() {
        currentPage.onLeave();
        currentPage = deque.getLast();
        deque.removeLast();

        currentStep = stepIndices.get(currentPage);

        stepProvider.showStep(currentStep, stepIndices.isEmpty(), false, // , isFirst, isLast
            "onBack: " + currentStep + "deque size: " + deque.size());
        currentPage.onEnter();
    }

    @Override
    public void onCancel() {		
        // TODO: parent.hide(); probably move to the StepProvider i-face
    }

    @Override
    public void onFinish() {
        currentPage.onFinish();		
    }

    @Override
    public WizardPage getCurrentPage() {
        return currentPage;
    }
}
