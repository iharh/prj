package mygwt.web.client.sentiments.resources;

import com.google.gwt.core.client.GWT;

//  Interface to represent the messages contained in resource bundle:
//      .../src/mygwt/web/client/sentiments/SentimentsMessages.properties'.
public interface SentimentsMessages extends com.google.gwt.i18n.client.Messages {
    SentimentsMessages INSTANCE = GWT.create(SentimentsMessages.class);

    @DefaultMessage("&nbsp;")
    @Key("nbsp")
    String nbsp();

    @DefaultMessage("<div style=\"height: 10px;\">&nbsp;</div>")
    @Key("divNbsp")
    String divNbsp();

    @DefaultMessage("Sentiment Export")
    @Key("sentimentExport")
    String sentimentExport();

    @DefaultMessage("Export")
    @Key("export")
    String export();

    @DefaultMessage("Cancel")
    @Key("cancel")
    String cancel();

    @DefaultMessage(" Include Tuned Sentiment Words.")
    @Key("includeTunedWords")
    String includeTunedWords();

    @DefaultMessage(" Include Exception Rules.")
    @Key("includeExceptionRules")
    String includeExceptionRules();

    String exportName();
    String exportDescription();
    String exportContent();

    // Recent Sentiment Exports

    String rceTitle();
    String rceText();
    String rceColumnName();
    String rceColumnDescription();
    String rceColumnTimestamp();
    String rceColumnParameters();
    String rceColumnFile();

    String rceNoExportsDefined();
}
