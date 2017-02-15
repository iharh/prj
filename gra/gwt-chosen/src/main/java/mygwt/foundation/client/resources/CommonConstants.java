package mygwt.foundation.client.resources;

import com.google.gwt.core.client.GWT;

public interface CommonConstants extends com.google.gwt.i18n.client.Messages {
    CommonConstants INSTANCE = GWT.create(CommonConstants.class);

    String EMPTY_STRING = "";

    /**
     * Translated "Back".
     * 
     * @return translated "Back"
     */
    @DefaultMessage("Back")
    @Key("back")
    String back();

    /**
     * Translated "Cancel".
     * 
     * @return translated "Cancel"
     */
    @DefaultMessage("Cancel")
    @Key("cancel")
    String cancel();

    /**
     * Translated "Close".
     * 
     * @return translated "Close"
     */
    @DefaultMessage("Close")
    @Key("close")
    String close();

    /**
     * Translated "Error".
     * 
     * @return translated "Error"
     */
    @DefaultMessage("Error")
    @Key("error")
    String error();

    /**
     * Translated "Export".
     * 
     * @return translated "Export"
     */
    @DefaultMessage("Export")
    @Key("export")
    String export();

    /**
     * Translated "Finish".
     * 
     * @return translated "Finish"
     */
    @DefaultMessage("Finish")
    @Key("finish")
    String finish();

    /**
     * Translated "Next".
     * 
     * @return translated "Next"
     */
    @DefaultMessage("Next")
    @Key("next")
    String next();

    /**
     * Translated "No".
     * 
     * @return translated "No"
     */
    @DefaultMessage("No")
    @Key("no")
    String no();

    /**
     * Translated "OK".
     * 
     * @return translated "OK"
     */
    @DefaultMessage("OK")
    @Key("ok")
    String ok();

    /**
     * Translated "Save".
     * 
     * @return translated "Save"
     */
    @DefaultMessage("Save")
    @Key("save")
    String save();

    /**
     * Translated "APPLY".
     * 
     * @return translated "APPLY"
     */
    @DefaultMessage("APPLY")
    @Key("upCaseApply")
    String upCaseApply();
    
    /**
     * Translated "Apply".
     * 
     * @return translated "Apply"
     */
    @DefaultMessage("Apply")
    @Key("apply")
    String apply();

    /**
     * Translated "Map".
     * 
     * @return translated "Map"
     */
    @DefaultMessage("Map")
    @Key("map")
    String map();

    /**
     * Translated "Map & Process".
     * 
     * @return translated "Map & Process"
     */
    @DefaultMessage("Map & Process")
    @Key("mapAndProcess")
    String mapAndProcess();
    
    /**
     * Translated "Unmap".
     * 
     * @return translated "Unmap"
     */
    @DefaultMessage("Unmap")
    @Key("unmap")
    String unmap();

    /**
     * Translated "CANCEL".
     * 
     * @return translated "CANCEL"
     */
    @DefaultMessage("CANCEL")
    @Key("upCaseCancel")
    String upCaseCancel();

    /**
     * Translated "EXPORT".
     * 
     * @return translated "EXPORT"
     */
    @DefaultMessage("EXPORT")
    @Key("upCaseExport")
    String upCaseExport();

    /**
     * Translated "Yes".
     * 
     * @return translated "Yes"
     */
    @DefaultMessage("Yes")
    @Key("yes")
    String yes();

    /**
     * Translated "All".
     * 
     * @return translated "All"
     */
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
