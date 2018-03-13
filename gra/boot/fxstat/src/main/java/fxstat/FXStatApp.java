package fxstat;

//import org.springframework.context.annotation.Bean;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

//import org.springframework.stereotype.*;

import lombok.extern.slf4j.Slf4j;

import reactor.core.publisher.Flux;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;

//@SpringBootApplication
//@Configuration
@EnableAutoConfiguration
//@ComponentScan
@Slf4j
public class FXStatApp implements CommandLineRunner {

    private String logRecTime(String s) {
        String chunks[] = s.split(" ", 3);
        return chunks[0] + " " + chunks[1];
    }

    private int logRecVal(String s) {
        if (s.endsWith("Initializing FX")) {
            return 1;
        } else if (s.endsWith("Terminating FX")) {
            return -1;
        } else {
            return 0;
        }
    }

    @Override
    public void run(String... args) throws Exception {
        // TODO: get from args
        // "d:/download/a.txt"
        final String fileName = "d:/download/7691/server/fxproc-2017-08-27.txt";
        log.info("start");
        try (
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("foo.txt")))
        ) {
            Flux
                .fromStream(br.lines())
                .map(s -> new LogRec(logRecTime(s), logRecVal(s)))
                .scanWith(() -> new LogRec("", 0), (acc, r) -> {
                    return new LogRec(r.getTime(), acc.getValue() + r.getValue());
                })
                .skip(1) // to skip first acc-value
                .subscribe(r -> {
                    log.info("{} {}", r.getTime(), r.getValue());
                    out.printf("%s %s\n", r.getTime(), r.getValue());
                })
            ;
        }
        log.info("finish");
    }
 
    public static void main(String[] args) {
        SpringApplication.run(FXStatApp.class, args);
    }
}
