package mygwt.web.client.sentiments.wizard.panels;

import mygwt.web.client.sentiments.wizard.WizardActionHandler;
import mygwt.web.client.sentiments.wizard.WizardPage;

import mygwt.foundation.client.widget.button.BackButton;
import mygwt.foundation.client.widget.button.NextButton;
import mygwt.foundation.client.widget.button.OkButton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

import com.google.gwt.user.client.ui.HorizontalPanel;

public class ButtonsPanel extends HorizontalPanel {
    private WizardActionHandler handler;

    private NextButton nextButton;
    private BackButton backButton;
    private OkButton finishButton;

    public ButtonsPanel(WizardActionHandler handler) {
        super();
        this.handler = handler;

        this.setWidth("100%");
        this.setBorderWidth(0);  //remove ???
        this.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
        
        nextButton = new NextButton("Next"); // TODO: i18n 
        nextButton.getElement().setId("nextButton");
        
        backButton = new BackButton("Back"); // TODO: i18n
        backButton.getElement().setId("backButton");
        
        finishButton = new OkButton("Finish"); // TODO: i18n
        finishButton.getElement().setId("finishButton");
        
        disableFinish();

        HorizontalPanel buttonPanel = new HorizontalPanel();
        
        onPageChanged(handler.getCurrentPage(), true, false);

        buttonPanel.setSpacing(3);
        buttonPanel.add(backButton);
        buttonPanel.setCellVerticalAlignment(backButton, HorizontalPanel.ALIGN_MIDDLE);
        buttonPanel.add(nextButton);
        buttonPanel.setCellVerticalAlignment(nextButton, HorizontalPanel.ALIGN_MIDDLE);
        buttonPanel.add(finishButton);
        buttonPanel.setCellVerticalAlignment(finishButton, HorizontalPanel.ALIGN_MIDDLE);

        this.add(buttonPanel);

        NextHandler nHandler = new NextHandler();
        nextButton.addClickHandler(nHandler);
        
        enableNext();

        BackHandler bHandler = new BackHandler();
        backButton.addClickHandler(bHandler);
        
        FinishHandler fHandler = new FinishHandler();
        finishButton.addClickHandler(fHandler);
        CloseHandler cHandler = new CloseHandler();
        finishButton.addClickHandler(cHandler);
    }

    public void clickNext() {
        this.nextButton.click();
    }
    
    public void onPageChanged(WizardPage currentPage, boolean isFirst, boolean isLast) {
        nextButton.setEnabled(!isLast);
        finishButton.setEnabled(isLast);
        backButton.setEnabled(!isFirst);
    }

    public void enableNext() {
        nextButton.setEnabled(true);
    }
    public void disableNext() {
        nextButton.setEnabled(false);
    }
    public void enableBack() {
        backButton.setEnabled(true);
    }
    public void disableBack() {
        backButton.setEnabled(false);
    }
    public void enableFinish() {
        finishButton.setEnabled(true);
    }
    public void disableFinish() {
        finishButton.setEnabled(false);
    }

    class NextHandler implements ClickHandler {
        // Fired when the user clicks on the Next Button.
        public void onClick(ClickEvent event) {
            handler.onNext();
        }
    }
    class BackHandler implements ClickHandler {
        // Fired when the user clicks on the Next Button.
        public void onClick(ClickEvent event) {
            handler.onBack();
        }
    }
    class FinishHandler implements ClickHandler {
        // Fired 1-time when the user clicks on the Next Button.
        public void onClick(ClickEvent event) {
            handler.onFinish();
        }
    }
    class CloseHandler implements ClickHandler {
        // Fired 2-time when the user clicks on the Next Button.
        public void onClick(ClickEvent event) {
            handler.onClose();
        }
    }
}
