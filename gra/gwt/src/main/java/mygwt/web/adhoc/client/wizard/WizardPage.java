package mygwt.web.adhoc.client.wizard;

import mygwt.web.client.OnlineHelpContext;

public interface WizardPage extends OnlineHelpContext {
	void onEnter();
	void onLeave();
	void onFinish();	
	boolean saveDataToModel();
	String getErrorMessage();
}
