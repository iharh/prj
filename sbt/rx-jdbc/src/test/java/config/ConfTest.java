package config;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;


import com.typesafe.config.ConfigFactory;
import com.typesafe.config.Config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class ConfTest {
    private static final Logger log = LoggerFactory.getLogger(ConfTest.class);

    private static File getOraPropFile(String hostId) {
        return new File("D:\\dev\\notes\\wrk\\clb\\hosts\\db-csi\\ora\\" + hostId + ".database.system.properties");
    }

    private static Config getOraConf(String hostId) {
        Config result = ConfigFactory.parseFile(getOraPropFile(hostId));
        assertNotNull(result);
        return result;
    }

    private static void printProp(final Config conf, String propName) {
        final String propVal = conf.getString("cmpDS." + propName);
        assertNotNull(propVal);
        log.info(propName + ": {}", propVal);
    }

    @Test
    public void testConf() throws Exception {
        final Config conf = getOraConf("wildblue1");

        printProp(conf, "url");
        printProp(conf, "username");
        printProp(conf, "password");
    }
}

