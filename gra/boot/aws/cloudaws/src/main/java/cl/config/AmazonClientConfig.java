package cl.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class AmazonClientConfig {
    // TODO: configure
    private static final String S3_ENDPOINT = "http://localhost:9000";
    
    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public AmazonS3 amazonS3Client(AWSCredentialsProvider credentialsProvider) {
        AwsClientBuilder.EndpointConfiguration endpointConfiguration =
                new AwsClientBuilder.EndpointConfiguration(S3_ENDPOINT, region);

        AmazonS3 result = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(endpointConfiguration) //.withRegion(region)
                .withCredentials(credentialsProvider)
                .build();

        log.info("configured s3 client: {}", result);
        return result;
    }
}
