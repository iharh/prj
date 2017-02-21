package mygwt.web.client.sentiments.upload.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.Messages;

public interface SentimentUploadMessages extends Messages {
    SentimentUploadMessages INSTANCE = GWT.create(SentimentUploadMessages.class);

    String skippedRowTitle(int top);

    String selectFileMess();

    String fileUploadErrorMess(String error);

    String fileUploadingMess();

    String fileUploadErrorCantSave();

    String fileUploadNegatorTuned();

    String fileUploadNegatorTunedCancel();

    String fetchingSampleDataMess();

    String fetchSampleDataOkMess();

    String fetchSampleDataErrorMess();

    String fetchSampleDataEmptyError();

    String fetchSampleDataMetadataError();

    String fetchSampleDataHeaderCellEmptyError();

    String fetchSampleDataDuplColNamesError(String colNum);

    String fetchSampleDataDigitColNamesError();

    String fetchSampleDataIOError();

    String fetchSampleDataCantOpenError();

    String fetchSampleDataHeaderFirstRowEmpty();

    String storeInprogressMess();

    String storeErrorMess();

    String storeError();

    String storeCheckTypeError();

    String storeCheckAttributesError();

    String storeCreatingAttributeMess();

    String storeSuccessful(String fileName);

    String storeSuccessfulResult(int processed, int failed);

    String runTmplPropose();

    String runTmplError();

    String runTmplErrroWithMess(String mess);

	String runTmplInpogress();

//	String howToFixColumnTypes();

	String selectedNamesAreReserved(String namesList, String append);

	String selectedNamesAreTooLong(String namesList, int lenght);

	String nonPositiveCredits(String file, long estimateVerbatim, long availableBalance);

	String notEnoughCredits(String file, long estimateVerbatim, long availableBalance);

    String rowsExceedYourVerbatim();

    String estimatedVerbatim(long estimateVerbatim);

    String availableVerbatimCredits(long balance);

    String errorOnDBSynchronizing();

    String importedWords(int count);

    String importedRules(int count);

    String skippedWord();

    String skippedWords(int count);

    String skippedRule();

    String skippedRules(int count);

    /**
     * #ButtonsPanel messages
     */

    @DefaultMessage("Cancel")
    @Key("cancelButton")
    String cancelButton();

    @DefaultMessage("Next")
    @Key("nextButton")
    String nextButton();

    @DefaultMessage("Back")
    @Key("backButton")
    String backButton();

    @DefaultMessage("Process Data")
    @Key("processDataButton")
    String processDataButton();

    @DefaultMessage("Online video tutorial")
    @Key("videoTutorialTitle")
    String videoTutorialTitle();

    /**
     * #FileSelectionPanel messages
     */

    @DefaultMessage("<h4>Step 1 of 2. File Selection</h4>")
    @Key("fileSelectionHeader")
    String fileSelectionHeader();

    @DefaultMessage("Tuned sentiment should be provided in the form of Microsoft Excel file with two separate worksheets for tuned words and exception rules correspondingly. Once your sentiment data is ready for import, use the 'Browse'(or 'Choose File') button to select your file, then press 'Next'.")
    @Key("helpHtml")
    String helpHtml();

    @DefaultMessage("Please select your Microsoft Excel file.")
    @Key("pleaseHtml")
    String pleaseHtml();

    @DefaultMessage("Please select your Microsoft Excel file to upload")
    @Key("pleaseSelectMsg")
    String pleaseSelectMsg();

    @DefaultMessage("Success.")
    @Key("statusSuccess")
    String statusSuccess();

    @DefaultMessage("Fail:")
    @Key("statusFail")
    String statusFail();

    /**
     * #FinishPanel messages
     */

    @DefaultMessage("After applying changes to the sentiment words and exception rules, I want to recalculate sentiment.")
    @Key("updateSentiments")
    String updateSentiments();

    @DefaultMessage("Step 2 of 2. Upload process.")
    @Key("step2of2")
    String step2of2();

    @DefaultMessage("Press the 'Process Data' button to apply changes from imported file to your project. Warning: This step is <b>irreversible</b>, make sure you have a fresh backup and/or have exported your sentiment tuning.")
    @Key("warningHtml")
    String warningHtml();

    @DefaultMessage("<b>Upload process details:</b>")
    @Key("uploadDetails")
    String uploadDetails();

    @DefaultMessage("No data found in the uploaded file")
    @Key("noDataInFile")
    String noDataInFile();

    @DefaultMessage("Processing uploaded data...")
    @Key("processingData")
    String processingData();

    @DefaultMessage("Done!")
    @Key("done")
    String done();

    @DefaultMessage("Skipped words rows")
    @Key("skippedWordsRows")
    String skippedWordsRows();

    @DefaultMessage("Word")
    @Key("wordTitle")
    String wordTitle();

    @DefaultMessage("Skipped rules rows")
    @Key("skippedRulesRows")
    String skippedRulesRows();

    @DefaultMessage("Rule")
    @Key("ruleTitle")
    String ruleTitle();

    /**
     * #SentimentUploadWizard messages
     */
    @DefaultMessage("Sentiment Upload Wizard.")
    @Key("sentimentUploadWizard")
    String sentimentUploadWizard();

    /**
     * #SkippedRowPanel messages
     */
    @DefaultMessage("Row number")
    @Key("rowNumber")
    String rowNumber();

    @DefaultMessage("Error message")
    @Key("errorMessage")
    String errorMessage();

    @DefaultMessage("Warning")
    @Key("warning")
    String warning();
}
