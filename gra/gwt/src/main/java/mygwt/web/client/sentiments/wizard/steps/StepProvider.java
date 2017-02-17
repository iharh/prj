package mygwt.web.client.sentiments.wizard.steps;

public interface StepProvider {
    public void showStep(int stepIdx, boolean isFirst, boolean isLast, String traceMsg);
}
