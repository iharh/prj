package tsconf;

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

    @Test
    public void testConf() throws Exception {
        Config conf = ConfigFactory.parseFile(new File("D:\\dev\\notes\\wrk\\clb\\hosts\\db-csi\\ora\\ajura.database.system.properties"));
        assertNotNull(conf);

        final String url = conf.getString("cmpDS.url");
        assertNotNull(url);

        log.info("url: {}", url);
    }
}

