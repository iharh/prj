package simple;

import simple.dto.BenchRequest;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.micrometer.core.instrument.MeterRegistry;
// https://github.com/micrometer-metrics/micrometer/blob/master/micrometer-core/src/main/java/io/micrometer/core/instrument/Timer.java
import io.micrometer.core.instrument.Timer;

import javax.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(produces = "application/json")
@Slf4j
public class SimpleController {

    @Autowired
    private MeterRegistry meterRegistry;

    @PostMapping("/bench")
    public String bench(@RequestBody BenchRequest benchRequest) throws Exception {
        StringBuilder result = new StringBuilder(benchRequest.toString());

        final TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        Timer timer = meterRegistry.timer("nlp.bench." + benchRequest.getId());
        final int numIter = benchRequest.getIter();
        for (int i = 0; i < numIter; ++i) {
            timer.record(() -> {
                benchIter(timeUnit, benchRequest.getFile());
            });
        }

        result.append("cnt: ");
        result.append(timer.count());
        result.append(" totalTime: ");
        result.append(timer.totalTime(timeUnit));
        result.append(" mean: ");
        result.append(timer.mean(timeUnit));
        result.append(" max: ");
        result.append(timer.max(timeUnit));

        return result.toString();
    }

    @GetMapping("/hello")
    public String hello(HttpServletRequest request) throws Exception {
        StringBuilder result = new StringBuilder("headers: ");

        Enumeration<String> headerNames = request.getHeaderNames();
        if (headerNames != null) {
            for (Enumeration<String> e = headerNames; e.hasMoreElements();) {
                String headerName = e.nextElement();
                result.append(",");
                result.append(headerName);
                result.append(":");
                result.append(request.getHeader(headerName));
            }
        }

        return result.toString();
    }

    @SneakyThrows(IOException.class)
    private void benchIter(TimeUnit timeUnit, String fileName) {
        final long sleepTime = 100;
        try (BufferedReader br = Files.newBufferedReader(Paths.get(fileName))) {
            timeUnit.sleep(sleepTime);
            br.lines().forEach(line ->
                System.out.println(line)
            );
        } catch (InterruptedException ignored) {
        }
    }
}
