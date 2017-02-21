package mygwt.foundation.client.resources;

import com.google.gwt.core.client.GWT;

public interface CommonConstants extends com.google.gwt.i18n.client.Messages {
    CommonConstants INSTANCE = GWT.create(CommonConstants.class);

    String EMPTY_STRING = "";

    @DefaultMessage("Back")
    @Key("back")
    String back();

    @DefaultMessage("Cancel")
    @Key("cancel")
    String cancel();

    @DefaultMessage("Close")
    @Key("close")
    String close();

    @DefaultMessage("Error")
    @Key("error")
    String error();

    @DefaultMessage("Export")
    @Key("export")
    String export();

    @DefaultMessage("Finish")
    @Key("finish")
    String finish();

    @DefaultMessage("Next")
    @Key("next")
    String next();

    @DefaultMessage("No")
    @Key("no")
    String no();

    @DefaultMessage("OK")
    @Key("ok")
    String ok();

    @DefaultMessage("Save")
    @Key("save")
    String save();

    @DefaultMessage("APPLY")
    @Key("upCaseApply")
    String upCaseApply();
    
    @DefaultMessage("Apply")
    @Key("apply")
    String apply();

    @DefaultMessage("Map")
    @Key("map")
    String map();

    @DefaultMessage("Map & Process")
    @Key("mapAndProcess")
    String mapAndProcess();
    
    @DefaultMessage("Unmap")
    @Key("unmap")
    String unmap();

    @DefaultMessage("CANCEL")
    @Key("upCaseCancel")
    String upCaseCancel();

    @DefaultMessage("EXPORT")
    @Key("upCaseExport")
    String upCaseExport();

    @DefaultMessage("Yes")
    @Key("yes")
    String yes();

    @DefaultMessage("All")
    @Key("all")
    String all();

    @DefaultMessage("N/A")
    @Key("na")
    String na();

    @DefaultMessage("On")
    @Key("on")
    String on();

    @DefaultMessage("Off")
    @Key("off")
    String off();
}
