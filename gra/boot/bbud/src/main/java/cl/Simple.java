package cl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Simple {

    public String tell(Integer n, Integer k) {
        log.info("start tell: {}, {}", n, k);
        return "abc n=" + n + ", k=" + k;
    }
}
