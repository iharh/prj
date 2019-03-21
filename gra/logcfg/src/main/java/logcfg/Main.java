package logcfg;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main {
    public static void main(String[] args) {
        log.info("begin");
        System.out.println("Hello logcfg!");
        System.out.println(System.getProperty("log4j2.debug"));
    }
}
