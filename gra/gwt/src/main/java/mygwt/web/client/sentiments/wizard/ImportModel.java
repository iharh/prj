package mygwt.web.client.sentiments.wizard;

import mygwt.portal.dto.SentimentUploadValidationResult;

public class ImportModel {
    private SentimentUploadValidationResult sentimentUploadValidationResult;

    public SentimentUploadValidationResult getSentimentUploadValidationResult() {
        return sentimentUploadValidationResult;
    }

    public void setSentimentUploadValidationResult(SentimentUploadValidationResult sentimentUploadValidationResult) {
        this.sentimentUploadValidationResult = sentimentUploadValidationResult;
    } 
}
