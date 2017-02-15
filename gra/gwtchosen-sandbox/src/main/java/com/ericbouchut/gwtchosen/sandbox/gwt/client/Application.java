package com.ericbouchut.gwtchosen.sandbox.gwt.client;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.watopi.chosen.client.event.ChosenChangeEvent;
import com.watopi.chosen.client.event.ChosenChangeEvent.ChosenChangeHandler;
import com.watopi.chosen.client.gwt.ChosenListBox;


public class Application
    implements EntryPoint
{

    @Override
    public void onModuleLoad()
    {
        testChosenListBox();
    }


    protected void testChosenListBox()
    {
        FlowPanel flowPanel = new FlowPanel();

        flowPanel.add(buildHtmlMessage());

        final ChosenListBox chosenListBox = buildChosenListBox();
        flowPanel.add(chosenListBox);

        Button button = new Button("chosenListBox.setSelectedIndex(0)");
        button.addClickHandler(new ClickHandler()
        {

            @Override
            public void onClick(ClickEvent event)
            {
                chosenListBox.setSelectedIndex(0);

            }
        });
        flowPanel.add(button);

        RootLayoutPanel.get().add(flowPanel);
    }


    protected HTML buildHtmlMessage()
    {
        SafeHtmlBuilder safeHtmlBuilder = new SafeHtmlBuilder();
        safeHtmlBuilder
                .appendEscapedLines("A ChosenChangeHandler is configured to open popup a window each time you select a list item.\n");
        safeHtmlBuilder
                .appendEscapedLines("\nFollow the 3 steps below to reproduce the bug.\n");
        safeHtmlBuilder
                .appendEscapedLines("Step 1: Select a list item (\"Java\" for instance). The expected window pops up\n");
        safeHtmlBuilder
                .appendEscapedLines("Step 2: Click the button labeled \"chosenListBox.setSelectedItem(0)\"\n");
        safeHtmlBuilder
                .appendEscapedLines("Step 3: Re-select the very same list item as before (\"Java\"). This time the expected window does not pop up because no ChosenChangeEvent is triggered.\n\n");
        SafeHtml message = safeHtmlBuilder.toSafeHtml();

        return new HTML(message);
    }


    protected ChosenListBox buildChosenListBox()
    {
        final ChosenListBox chosenListBox = new ChosenListBox();

        chosenListBox.addChosenChangeHandler(new ChosenChangeHandler()
        {

            @Override
            public void onChange(ChosenChangeEvent event)
            {
                if (event.isSelection()) {
                    Window.alert("You selected \"" + event.getValue() + "\"");
                }
            }
        });
        populateChosenListBox(chosenListBox);

        return chosenListBox;
    }


    protected void populateChosenListBox(ChosenListBox chosenListBox)
    {
        chosenListBox.addItem("LOL");

        chosenListBox.addGroup("Static Languages");
        chosenListBox.addItemToGroup("Java");
        chosenListBox.addItemToGroup("C++");

        chosenListBox.addGroup("Dynamic Languages");
        chosenListBox.addItemToGroup("Ruby");
        chosenListBox.addItemToGroup("Python");
        chosenListBox.addItemToGroup("Javascript");
        chosenListBox.addItemToGroup("Lisp");

        chosenListBox.update();
    }

}
