.web.server.SentimentImportServiceAction

portal/web
.projectlist.web.client.

public final class ProjectApplicationContext extends ApplicationContext {
    ...
    public SentimentUploadServiceAsync getSentimentUploadService() {
        if (sentimentUploadService == null) {
        	sentimentUploadService = (SentimentUploadServiceAsync) GWT.create(SentimentUploadService.class);
            ((ServiceDefTarget) sentimentUploadService).setServiceEntryPoint(
            	Service.SENTIMENT_UPLOAD_SERVICE.getAbsoluteUrl() + ".action");
            ((ServiceDefTarget) sentimentUploadService).setRpcRequestBuilder(CsrfRpcRequestBuilder.getInstance(this));
        }
        return sentimentUploadService;
    }
    ...
}

gradle --stop
    rm -rf .gradle/

gradle
    clean
    build
    war
    jettyRunWar
