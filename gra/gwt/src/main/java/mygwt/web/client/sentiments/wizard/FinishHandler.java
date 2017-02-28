package mygwt.web.client.sentiments.wizard;

public interface FinishHandler {
    void onImport(boolean updateSentences);
    void onRecentSentimentsExport();
    void onCurrentExport();
}
