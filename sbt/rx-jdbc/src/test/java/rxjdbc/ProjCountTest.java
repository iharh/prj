package rxjdbc;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;


import com.typesafe.config.ConfigFactory;
import com.typesafe.config.Config;

import com.github.davidmoten.rx.jdbc.Database;
import com.github.davidmoten.rx.jdbc.tuple.Tuple2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import java.util.List;

public class ProjCountTest {
    private static final Logger log = LoggerFactory.getLogger(ProjCountTest.class);

    private static File getOraPropFile(String hostId) {
        return new File("D:\\dev\\notes\\wrk\\clb\\hosts\\db-csi\\ora\\" + hostId + ".database.system.properties");
    }

    private static Config getOraConf(String hostId) {
        Config result = ConfigFactory.parseFile(getOraPropFile(hostId));
        assertNotNull(result);
        return result;
    }

    private static String getDbProp(final Config conf, String propName) {
        final String result = conf.getString("cmpDS." + propName);
        assertNotNull(result);
        log.info(propName + ": {}", result);
        return result;
    }


    private static Database getPgDb(String url, String usr, String pwd) throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        return Database.from(url, usr, pwd);
    }

    private static Database getOraDb(String url, String usr, String pwd) throws ClassNotFoundException {
        Class.forName("oracle.jdbc.OracleDriver");
        return Database.from(url, usr, pwd);
    }


    private static Database getDb() throws ClassNotFoundException {
        //return getOraDb("jdbc:oracle:thin:@//localhost:1521/ihorcl", "win_ss", "clb");
        final Config conf = getOraConf("yellow5");
        return getOraDb(
            getDbProp(conf, "url"),
            getDbProp(conf, "username"),
            getDbProp(conf, "password") // STG_BB_ECC
        );
    }

    @Test
    public void testProj() throws Exception {
        Database db = getDb();

        long cnt = db
            .select("select count(*) from cb_project")
            .getAs(Long.class)
            //.doOnEach(RxUtil.log())
            .toBlocking()
            .single();

        log.info("project cnt -> " + cnt);
// .select("select name from person where name > ? order by name")
// .parameter("ALEX")
        assertTrue(true);
    }
}
