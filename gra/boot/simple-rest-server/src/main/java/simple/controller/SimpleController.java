package simple;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

import io.micrometer.core.instrument.MeterRegistry;
// https://github.com/micrometer-metrics/micrometer/blob/master/micrometer-core/src/main/java/io/micrometer/core/instrument/Timer.java
import io.micrometer.core.instrument.Timer;

import javax.servlet.http.HttpServletRequest;

import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(produces = "application/json")
@Slf4j
public class SimpleController {

    @Autowired
    private MeterRegistry meterRegistry;

    @GetMapping("/hello")
    public String hello(HttpServletRequest request) throws Exception {
        StringBuilder result = new StringBuilder("headers: ");

        final TimeUnit timeUnit = TimeUnit.MILLISECONDS;
        Timer timer = meterRegistry.timer("nlp.bench");
        for (long i = 0; i < 10; ++i) {
            final long sleepTime = 100 + i*10;
            timer.record(() -> {
                try {
                    timeUnit.sleep(sleepTime);
                } catch (InterruptedException ignored) { }
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
}
