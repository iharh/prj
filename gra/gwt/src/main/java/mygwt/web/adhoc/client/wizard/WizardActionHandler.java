package mygwt.web.adhoc.client.wizard;

public interface WizardActionHandler {
    void onNext();
    void onBack();
    void onCancel();
    void onFinish();	
    WizardPage getCurrentPage();	
}
