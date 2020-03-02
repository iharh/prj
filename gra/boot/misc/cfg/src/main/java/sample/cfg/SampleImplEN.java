package sample.cfg;

import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public class SampleImplEN implements SampleIface {
    private String lexiconExportUrl;

    public SampleImplEN(String lexiconExportUrl) {
        this.lexiconExportUrl = lexiconExportUrl;
    }

    @Override
    public String getId() {
        return "en: " + buildUrl(lexiconExportUrl, "local-inst", "LEXTYPE", 0);
    }

    private static String buildUrl(String lexiconExportUrl, String instanceId, String lexiconType, long accountId) {
        Map<String, String> uriVariables = Map.of("instance-id", instanceId,
                "lexicon-type", lexiconType, "account-id",  Long.toString(accountId));
        return UriComponentsBuilder.newInstance()
                .fromHttpUrl(lexiconExportUrl)
                .buildAndExpand(uriVariables)
                .toUriString();
    }
}
