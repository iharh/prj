package mygwt.web.client.sentiments.wizard;

import mygwt.foundation.client.csrf.ProjectIdAware;

import mygwt.web.client.sentiments.wizard.panels.ButtonsPanel;

import mygwt.foundation.client.widget.dialog.BaseDialogBox;
import mygwt.foundation.client.widget.button.CancelButton;
import mygwt.foundation.client.widget.button.OkButton;
import mygwt.foundation.client.widget.list.GroupedListBox;

import mygwt.web.adhoc.client.wizard.WizardPage;

import mygwt.web.client.sentiments.resources.SentimentsMessages;

import com.google.gwt.core.client.GWT;

import com.google.gwt.dom.client.Style.Unit;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineLabel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SentimentsWizard extends BaseDialogBox implements WizardActionHandler, ProjectIdAware {
    private static final int STEPS_USUAL_HEIGHT = 250;
    private static final int INFO_USUAL_HEIGHT = 200;	
    private static final int STEPS_AVAILABLE_HEIGHT = INFO_USUAL_HEIGHT + STEPS_USUAL_HEIGHT;

    private final long projectId;

    private DeckPanel steps;

    /*private FileSelectionPanel step1;
    private SourceSelectionPanel step2;
    private NaturalKeySelectionPanel step3;
    private BodySelectionPanel step4;
    private AttributeSelectionPanel step5;
    private AttributeMappingPanel step51;
    //private DateSelectionPanel step6;
    private ModifiedDateSelectionPanel step7;
    private FinishPanel step8;*/
    private ButtonsPanel buttonsPanel;

    // ???
    private static final int borderW     = 0; // 1 for borders vis-n
    private static final int allW        = 670;
    private static final int allH        = 525;
    private static final String textBoxW = "470px"; // 450px
    private static final int labelW      = 150;
    private static final int spacing     = 12; // 10

    private ClickHandler closeButtonHandler;

    private SentimentsMessages msgs;

    @Override
    public long getProjectId() {
        return projectId;
    }

    public SentimentsWizard(long projectId) {
        super(SentimentsMessages.INSTANCE.rceTitle(), allW, allH); // change title !!!
        this.projectId = projectId;

        msgs = SentimentsMessages.INSTANCE;
        //hack for IE6/7(CMP-15701)
        //DeckPanel hackPanel = new DeckPanel();
        //hackPanel.setHeight("450px");
        //setWidget(hackPanel);

        setWidget(createDialogContents());

	setAutoHideEnabled(false);
        //hide();
    }

    private Widget createDialogContents(boolean visible) {
        /*
        DockLayoutPanel panel = new DockLayoutPanel(Unit.PX);

        panel.setWidth(allW + "px");
        panel.setHeight(allH + "px");

        panel.addNorth(createMainPanel(), 250);
        panel.addSouth(createButtonPanel(), 50);

        return panel;
        */
        dialogVPanel = new VerticalPanel();
        dialogVPanel.setStyleName("AdHocWizardMainPanel");
        dialogVPanel.setSpacing(spacing);
        dialogVPanel.setSize("100%", "100%");

        steps = new DeckPanel();

        /*step1 = new FileSelectionPanel(this);
        step2 = new SourceSelectionPanel(this);
        step3 = new NaturalKeySelectionPanel(this);
        step4 = new BodySelectionPanel(this);
        step5 = new AttributeSelectionPanel(this);
        step51 = new AttributeMappingPanel(this);
        //step6 = new DateSelectionPanel(this);
        step7 = new ModifiedDateSelectionPanel(this);
        step8 = new FinishPanel(this);*/

        buttonsPanel = new ButtonsPanel(this);

        configureWizard();

        return dialogVPanel;
    }

    private void configureWizard() {
        dialogVPanel.clear();
        steps.add(step1);  // index 0
        steps.add(step2);  // index 1
        steps.add(step3);  // index 2
        steps.add(step4);  // index 3
        steps.add(step5);  // index 4
        steps.add(step51);  // index 4
        //steps.add(step6);  // index 5
        steps.add(step7);  // index 6
        steps.add(step8);  // index 7
        
        currentPage = step1;
        
        steps.showWidget(currentStep);
        
        dialogVPanel.add(steps);

        buttonsPanel.disableBack();
        buttonsPanel.enableNext();
        buttonsPanel.disableFinish();

        dialogVPanel.add(buttonsPanel);
        // ??? dialogVPanel.add(placeHolder);
    }

    @Override
    protected void onOpen() {
        super.onOpen();
        if (currentPage != null) {
            currentPage.onEnter();
        }	
    }

    // my stuff ...
/*
    private Panel createMainPanel() {
        VerticalPanel infoPanel = new VerticalPanel();
        infoPanel.setBorderWidth(borderW);
        //infoPanel.setBorderWidth(borderW);
	infoPanel.setSpacing(spacing); // too rough

        infoPanel.add(new InlineLabel("some text"));

        GroupedListBox listBox = new GroupedListBox();
        listBox.setVisibleItemCount(1);
	listBox.insertItem("grp1", "attr11", "disp11");
	listBox.insertItem("grp1", "attr12", "disp12");
	listBox.insertItem("grp1", "attr13", "disp13");
	listBox.insertItem("grp2", "attr21", "disp21");
	listBox.insertItem("grp2", "attr22", "disp22");
	listBox.insertItem("grp2", "attr23", "disp23");

        infoPanel.add(listBox);

        return infoPanel;
    }

    private Panel createButtonPanel() {
        closeButtonHandler = new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                SampleDialog.this.hide();
            }
        };

        final OkButton     exportBtn = new OkButton(SentimentsMessages.INSTANCE.export());
        final CancelButton cancelBtn = new CancelButton(SentimentsMessages.INSTANCE.cancel());

        exportBtn.addClickHandler(closeButtonHandler);
        cancelBtn.addClickHandler(closeButtonHandler);

        DockLayoutPanel btnPanel = new DockLayoutPanel(Unit.PX);
        btnPanel.setWidth(allW + "px");
        btnPanel.setHeight("50px");

        HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setBorderWidth(borderW);

        buttonPanel.add(exportBtn);
        buttonPanel.add(new HTML(SentimentsMessages.INSTANCE.nbsp()));
        buttonPanel.add(cancelBtn);

        btnPanel.addEast(buttonPanel, 165);

        return btnPanel;
    }
*/
}
