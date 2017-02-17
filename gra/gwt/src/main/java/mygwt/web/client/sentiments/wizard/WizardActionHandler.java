package mygwt.web.client.sentiments.wizard;

public interface WizardActionHandler {
    void onNext();
    void onBack();
    void onFinish();	
    WizardPage getCurrentPage();	
}
