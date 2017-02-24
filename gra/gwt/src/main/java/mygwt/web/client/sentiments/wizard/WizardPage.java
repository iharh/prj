package mygwt.web.client.sentiments.wizard;

public interface WizardPage {
    void onEnter();
    boolean onLeave();
    void onFinish();	
}
