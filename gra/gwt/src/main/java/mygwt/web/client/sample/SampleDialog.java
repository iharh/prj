package mygwt.web.client.sample;

import mygwt.foundation.client.csrf.ProjectIdAware;

import mygwt.foundation.client.widget.dialog.BaseDialogBox;
import mygwt.foundation.client.widget.button.CancelButton;
import mygwt.foundation.client.widget.button.OkButton;
import mygwt.foundation.client.widget.list.GroupedListBox;

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

public class SampleDialog extends BaseDialogBox implements ProjectIdAware {
    private static final int borderW     = 0; // 1 for borders vis-n
    private static final int allW        = 640;
    private static final int allH        = 370;
    private static final String textBoxW = "470px"; // 450px
    private static final int labelW      = 150;
    private static final int spacing     = 12;

    private ClickHandler closeButtonHandler;

    private SentimentsMessages msgs;

    @Override
    public long getProjectId() {
        return 0;
    }

    public SampleDialog() {
        super(SentimentsMessages.INSTANCE.rceTitle(), allW, allH);
        msgs = SentimentsMessages.INSTANCE;
        setWidget(createDialogContents());
        hide();
    }

    private Widget createDialogContents() {
        DockLayoutPanel panel = new DockLayoutPanel(Unit.PX);

        panel.setWidth(allW + "px");
        panel.setHeight(allH + "px");

        panel.addNorth(createMainPanel(), 250);
        panel.addSouth(createButtonPanel(), 50);

        return panel;
    }

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
}
