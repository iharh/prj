package sample;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.ComponentScan;
//import org.springframework.stereotype.*;

import lombok.extern.slf4j.Slf4j;

//@SpringBootApplication
//@Configuration
@EnableAutoConfiguration
@ComponentScan({"sample"})
@Slf4j
public class SampleApp implements CommandLineRunner {
    @Autowired
    private SampleIface sampleIface;

    @Override
    public void run(String... args) throws Exception {
        log.info("run id: {}", sampleIface.getId());
    }
 
    public static void main(String[] args) {
        SpringApplication.run(SampleApp.class, args);
    }
}
