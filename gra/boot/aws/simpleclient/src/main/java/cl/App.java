package cl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.util.List;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class App implements CommandLineRunner {

    @Autowired
    private AwsSettings s3Settings;

    @Autowired
    private AmazonS3 s3Client;
    
    public String getGreeting() {
        return "Hello world.";
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("greeting: {}", getGreeting());

        try {
            ObjectListing listing = s3Client.listObjects(s3Settings.getBucket());
            List<S3ObjectSummary> summaries = listing.getObjectSummaries();

            while (listing.isTruncated()) {
                listing = s3Client.listNextBatchOfObjects(listing);
                summaries.addAll(listing.getObjectSummaries());
            }

            summaries.stream()
                .map(S3ObjectSummary::getKey)
                .forEach(key -> log.info("{}", key));
            
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());

        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered an internal error while trying to communicate with S3, such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
