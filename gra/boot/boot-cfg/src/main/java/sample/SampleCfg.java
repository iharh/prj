package sample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@Configuration
public class SampleCfg {
    @Bean
    @ConditionalOnProperty(havingValue = "en", prefix = "lang", name = "id")
    public SampleIface sampleBeanEN() {
        return new SampleImplEN();
    }
    @Bean
    @ConditionalOnProperty(havingValue = "de", prefix = "lang", name = "id")
    public SampleIface sampleBeanDE() {
        return new SampleImplDE();
    }
}
