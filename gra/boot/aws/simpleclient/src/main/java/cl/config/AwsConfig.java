package cl.config;

import cl.AwsSettings;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class AwsConfig {

    private static final String AWS_SIGNER_TYPE = "AWSS3V4SignerType";

    @Bean
    @ConfigurationProperties("aws")
    public AwsSettings awsSettings() {
        AwsSettings result= new AwsSettings();
        /*
        result.setBucket("clbbucket");
        result.setEndpoint("http://localhost:9000");
        result.setRegion("us-west-2");
        */
        return result;
    }

    @Bean
    public AmazonS3 amazonS3(AwsSettings awsSettings) {
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setSignerOverride(AWS_SIGNER_TYPE);

        log.info("access key id: {}, secret key: {}", System.getenv("AWS_ACCESS_KEY_ID"), System.getenv("AWS_SECRET_ACCESS_KEY"));
        
        final String endpoint = awsSettings.getEndpoint();
        final String region = awsSettings.getRegion();

        log.info("endpoint : {}, region: {}", endpoint, region);

        AmazonS3ClientBuilder clientBuilder = AmazonS3ClientBuilder
                .standard()
                .withPathStyleAccessEnabled(true)
                .withClientConfiguration(clientConfiguration);

        AmazonS3 amazonS3 = null;
        if (endpoint == null) {
            log.info("using AmazonS3 region based config");
            amazonS3 = clientBuilder
                    .withRegion(region)
                    .build();
        } else {
            log.info("using AmazonS3 endpoint based config");
            AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                    endpoint, region);

            amazonS3 = clientBuilder
                    .withEndpointConfiguration(endpointConfiguration)
                    .build();
        }

        return amazonS3;
    }

}
