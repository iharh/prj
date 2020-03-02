package sample.cfg;

public class SampleImplEN implements SampleIface {
    private String lexiconExportUrl;

    public SampleImplEN(String lexiconExportUrl) {
        this.lexiconExportUrl = lexiconExportUrl;
    }

    @Override
    public String getId() {
        return "en: " + lexiconExportUrl;
    }
}
