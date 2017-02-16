package mygwt.web.client.sentiments.wizard;

public interface StepProvider {
    public void showStep(int stepIdx, boolean isFirst, boolean isLast, String traceMsg);
}
