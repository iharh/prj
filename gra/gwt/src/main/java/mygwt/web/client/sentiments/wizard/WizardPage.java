package mygwt.web.client.sentiments.wizard;

public interface WizardPage {
    void onEnter();
    boolean onLeave(boolean isNext);
    void onFinish();	
    void onClose();	
}
