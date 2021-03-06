package mygwt.web.client.sentiments.wizard.steps;

import mygwt.web.client.sentiments.wizard.WizardPage;
import mygwt.web.client.sentiments.wizard.WizardActionHandler;

import mygwt.web.client.utils.LogUtils;

import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;

public class StepNavigator implements WizardActionHandler {
    private StepProvider stepProvider;

    private int stepsSize;
    private Set<WizardPage> pages;
    private Map<WizardPage, Integer> stepIndices;
    private Map<WizardPage, NextPageDetector> nextPageDetectors;

    private WizardPage currentPage;
    private int currentStep;

    private LinkedList<WizardPage> deque;

    public StepNavigator(StepProvider stepProvider) {
        this.stepProvider = stepProvider;

        pages = new HashSet<WizardPage>();
        deque = new LinkedList<WizardPage>();
        stepIndices = new HashMap<WizardPage, Integer>();
        nextPageDetectors = new HashMap<WizardPage, NextPageDetector>();
    }

    public<T extends WizardPage> T addPage(T panel) {
        pages.add(panel);
        stepIndices.put(panel, stepsSize);
        ++stepsSize;
        return panel;
    }

    public void addNextPageDetector(WizardPage page, NextPageDetector detector) {
        nextPageDetectors.put(page, detector);
    }

    public void addNextPageTo(WizardPage page, final WizardPage nextPage) {
        nextPageDetectors.put(page, new NextPageDetector() { @Override public WizardPage next() { return nextPage; } });
    }

    public void start(WizardPage page) {
        currentPage = page;
        currentStep = stepIndices.get(currentPage);
        deque.clear();

        showStep("clear");
    }

    private void showStep(String name) {
        stepProvider.showStep(currentStep, deque.isEmpty(), !nextPageDetectors.containsKey(currentPage),
            name + ": " + currentStep + " deque size: " + deque.size());
    }

    @Override
    public void onNext() {
        if (!currentPage.onLeave(true)) {
            return;
        }
        deque.add(currentPage);
        //LogUtils.log("added to deque. size: " + deque.size());

        NextPageDetector nextPageDetector = nextPageDetectors.get(currentPage);
        currentPage = nextPageDetector.next();
        currentStep = stepIndices.get(currentPage);

        showStep("onNext");
        currentPage.onEnter();
    }

    @Override
    public void onBack() {
        currentPage.onLeave(false);
        currentPage = deque.getLast();
        deque.removeLast();
        //LogUtils.log("removed from deque. size: " + deque.size());

        currentStep = stepIndices.get(currentPage);

        showStep("onBack");
        currentPage.onEnter();
    }

    @Override
    public void onFinish() {
        currentPage.onFinish();		
    }

    @Override
    public void onClose() {
        currentPage.onClose();		
    }

    @Override
    public WizardPage getCurrentPage() {
        return currentPage;
    }
}
