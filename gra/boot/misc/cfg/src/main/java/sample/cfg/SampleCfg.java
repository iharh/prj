package sample.cfg;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Configuration
public class SampleCfg {
    @Value("${sample.lexicon.export-url}")
    private String lexiconExportUrl;
    
    @Bean
    @ConditionalOnProperty(havingValue = "en", prefix = "lang", name = "id")
    public SampleIface sampleBeanEN() {
        return new SampleImplEN(lexiconExportUrl);
    }
    @Bean
    @ConditionalOnProperty(havingValue = "de", prefix = "lang", name = "id")
    public SampleIface sampleBeanDE() {
        return new SampleImplDE(lexiconExportUrl);
    }
}
