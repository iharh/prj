package mdmwrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String [] args) {
        try {
            while (true) {
                log.info("Hello aaa");
                Thread.sleep(2000);
            }
        } catch (InterruptedException e) {
                log.info("Caught InterruptedException");
        }
    }
};

