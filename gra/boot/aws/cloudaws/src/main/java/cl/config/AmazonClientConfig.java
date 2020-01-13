package cl.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

@Configuration
public class AmazonClientConfig {
    @Value("${cloud.aws.region.static}")
    private String region;

    @Bean
    public AmazonS3 amazonS3Client(AWSCredentialsProvider credentialsProvider) {
        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(credentialsProvider)
                .withRegion(region)
                .build();
    }
}
