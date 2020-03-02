package sample.cfg;

public class SampleImplDE implements SampleIface {
    private String lexiconExportUrl;

    public SampleImplDE(String lexiconExportUrl) {
        this.lexiconExportUrl = lexiconExportUrl;
    }

    @Override
    public String getId() {
        return "de: " + lexiconExportUrl;
    }
}
