package mygwt.common.client.widget.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface CommonClientWidgetMessages extends Messages {
    CommonClientWidgetMessages INSTANCE = GWT.create(CommonClientWidgetMessages.class);

    @DefaultMessage("Project: ")
    @Key("labProject")
    String labProject();

    @DefaultMessage("Add Project")
    @Key("linkAddProject")
    String linkAddProject();

    @DefaultMessage("Go to projects list")
    @Key("linkGoToPrjList")
    String linkGoToPrjList();

    @DefaultMessage("{0} of {1}")
    @Key("pageOf")
    String pageOf(int arg0, int arg1);

    @DefaultMessage("Select")
    @Key("catTreeSelect")
    String catTreeSelect();

    @DefaultMessage("Model: ")
    @Key("catTreeModel")
    String catTreeModel();

    @DefaultMessage("Select model and drag-drop a category to the rules area.")
    @Key("catTreeSelectModel")
    String catTreeSelectModel();

    @DefaultMessage("Message")
    @Key("dlgMessage")
    String dlgMessage();

    @DefaultMessage("Category Tree")
    @Key("catTreeTree")
    String catTreeTree();

    @DefaultMessage("Add/Merge Theme")
    @Key("addMergeTheme")
    String addMergeTheme();

    @DefaultMessage("<span style=''white-space: nowrap''><img src=\"{0}\"''/><span style=''padding-left: 5px''>{1}</span></span>")
    @Key("menuItemHtml")
    String menuItemHtml(String imageUrl, String text);
}
