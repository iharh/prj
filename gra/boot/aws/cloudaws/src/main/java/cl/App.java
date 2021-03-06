package cl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.core.io.s3.PathMatchingSimpleStorageResourcePatternResolver;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.amazonaws.services.s3.AmazonS3;

import java.io.InputStream;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class App implements CommandLineRunner {
    @Value("${cloud.aws.bucket}")
    private String bucket;

    @Autowired
    private AmazonS3 s3client;
    
    @Autowired
    private ResourceLoader resourceLoader;

    private ResourcePatternResolver resourcePatternResolver;

    @Autowired
    public void setupResolver(ApplicationContext applicationContext, AmazonS3 amazonS3){
        this.resourcePatternResolver = new PathMatchingSimpleStorageResourcePatternResolver(amazonS3, applicationContext);
    }

    public String getGreeting() {
        return "Hello world.";
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("greeting: {} bucket: {}", getGreeting(), bucket);

        Resource[] resources = resourcePatternResolver.getResources("s3://" + bucket + "/*.txt");
        log.info("found");

        // Resource resource = resourceLoader.getResource("s3://" + bucket + "/" + "1.txt");
        //InputStream inputStream = resource.getInputStream();
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
