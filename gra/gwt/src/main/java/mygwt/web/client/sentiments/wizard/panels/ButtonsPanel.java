package mygwt.web.adhoc.client.sentiments.wizard.panels;

import mygwt.web.adhoc.client.wizard.WizardActionHandler;
import mygwt.web.adhoc.client.wizard.WizardPage;

import mygwt.foundation.client.widget.button.BackButton;
import mygwt.foundation.client.widget.button.CancelButton;
import mygwt.foundation.client.widget.button.NextButton;
import mygwt.foundation.client.widget.button.OkButton;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
//import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
//import com.google.gwt.user.client.ui.Image;

public class ButtonsPanel extends HorizontalPanel { // DockLayoutPanel
    private WizardActionHandler handler;

    private CancelButton cancelButton;
    private NextButton nextButton;
    private BackButton backButton;
    private OkButton finishButton;
    //private HTML videoTutorialLink;
    //private Image waitImage;

    public ButtonsPanel(WizardActionHandler handler, String finishButtonCaption) {
        super();
        this.handler = handler;

        this.setWidth("100%");
        this.setBorderWidth(0);  //remove ???
        this.setHorizontalAlignment(HorizontalPanel.ALIGN_RIGHT);
        
        //waitImage = new AjaxLoaderImage();
        //waitImage.setVisible(false);

        cancelButton = new CancelButton("Cancel"); // TODO: i18n
        cancelButton.getElement().setId("cancelButton"); //$NON-NLS-1$
        
        nextButton = new NextButton("Next"); // TODO: i18n 
        nextButton.getElement().setId("nextButton"); //$NON-NLS-1$
        
        backButton = new BackButton("Back"); // TODO: i18n
        backButton.getElement().setId("backButton"); //$NON-NLS-1$
        
        finishButton = new OkButton("Finish"); // TODO: i18n
        finishButton.getElement().setId("finishButton");
        
        disableFinish();

        HorizontalPanel buttonPanel = new HorizontalPanel();
        
        //buttonPanel.add(waitImage);
        //buttonPanel.setCellVerticalAlignment(waitImage, HorizontalPanel.ALIGN_MIDDLE);
        
        //videoTutorialLink = new HTML(""); //$NON-NLS-1$
        //videoTutorialLink.setVisible(false);
        //buttonPanel.add(videoTutorialLink);
        //buttonPanel.setCellVerticalAlignment(videoTutorialLink, HorizontalPanel.ALIGN_MIDDLE);

        onPageChanged(handler.getCurrentPage(), true, false);

        buttonPanel.setSpacing(3);
        buttonPanel.add(backButton);
        buttonPanel.setCellVerticalAlignment(backButton, HorizontalPanel.ALIGN_MIDDLE);
        buttonPanel.add(nextButton);
        buttonPanel.setCellVerticalAlignment(nextButton, HorizontalPanel.ALIGN_MIDDLE);
        buttonPanel.add(finishButton);
        buttonPanel.setCellVerticalAlignment(finishButton, HorizontalPanel.ALIGN_MIDDLE);
        buttonPanel.add(cancelButton);
        buttonPanel.setCellVerticalAlignment(cancelButton, HorizontalPanel.ALIGN_MIDDLE);

        this.add(buttonPanel);

        CancelHandler cHandler = new CancelHandler();
        cancelButton.addClickHandler(cHandler);

        NextHandler nHandler = new NextHandler();
        nextButton.addClickHandler(nHandler);
        
        enableNext();

        BackHandler bHandler = new BackHandler();
        backButton.addClickHandler(bHandler);
        
        FinishHandler fHandler = new FinishHandler();
        finishButton.addClickHandler(fHandler);
    }

    public void clickNext() {
        this.nextButton.click();
    }
    
    public void onPageChanged(WizardPage currentPage, boolean isFirst, boolean isLast) {
        /*
        if (currentPage != null && currentPage.getOnlineVideoURL() != null) {
            String onlineVideoURL = handler.getCurrentPage().getOnlineVideoURL();
            String link = getVideoTutorialLink(onlineVideoURL);
            videoTutorialLink.setHTML(link);
            videoTutorialLink.setVisible(true);
        }
        */
        nextButton.setEnabled(!isLast);
        finishButton.setEnabled(isLast);
        backButton.setEnabled(!isFirst);
    }
/*
    public static String getVideoTutorialLink(String onlineVideoURL) {
        String helpVersion = new HostPageProperties().getVersion();
        HelpURLGeneratorGWT helpURLGeneratorGWT = new HelpURLGeneratorGWT(helpVersion, onlineVideoURL);
        String link = helpURLGeneratorGWT.getImageVideoLink("images/bt_16x16/icon_video.gif", AdHocMessages.INSTANCE.onlineVideoTutorial());
        return link;
    }
*/
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

    class CancelHandler implements ClickHandler, KeyUpHandler {
        // Fired when the user clicks on the sendButton.
        public void onClick(ClickEvent event) {
            handler.onCancel();
        }
        // Fired when the user types in the nameField. TODO: ???
        public void onKeyUp(KeyUpEvent event) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE) {
                handler.onCancel();
            }
        }
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
        // Fired when the user clicks on the Next Button.
        public void onClick(ClickEvent event) {
            handler.onFinish();
        }
    }
/*
    public void disableWaitImage() {
        waitImage.setVisible(false);
    }
    
    public void enableWaitImage() {
        waitImage.setVisible(true);
    }
*/
}
