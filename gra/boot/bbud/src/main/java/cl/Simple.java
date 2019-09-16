package cl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Simple {

    public String tell() {
        log.info("start tell");
        return "abc";
    }
}
