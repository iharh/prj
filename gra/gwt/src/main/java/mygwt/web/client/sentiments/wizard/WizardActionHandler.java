package mygwt.web.client.sentiments.wizard;

public interface WizardActionHandler {
    void onNext();
    void onBack();
    void onFinish();	
    void onClose();	
    WizardPage getCurrentPage();	
}
