package mygwt.web.server;

import mygwt.portal.dto.SentimentUploadValidationResult;

public class SentimentTransferCart {
    private SentimentUploadValidationResult sentimentUploadValidationResult;

    public void setSentimentUploadValidationResult(SentimentUploadValidationResult sentimentUploadValidationResult) {
        this.sentimentUploadValidationResult = sentimentUploadValidationResult;
    }
    public SentimentUploadValidationResult getSentimentUploadValidationResult() {
        return sentimentUploadValidationResult;
    }
}
