package rxjdbc;

import com.typesafe.config.ConfigFactory;
import com.typesafe.config.Config;

import com.github.davidmoten.rx.jdbc.Database;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBUtils {
    private static final Logger log = LoggerFactory.getLogger(DBUtils.class);

    private static final String DB_CFG_ROOT = "D:\\dev\\notes\\wrk\\clb\\hosts\\db\\"; 
    private static final String DB_CFG_NAME = ".database.system.properties";

    private static String getDbProp(final Config conf, String propName) {
        final String result = conf.getString("cmpDS." + propName);
        log.debug(propName + ": {}", result);
        return result;
    }

    public static Database getConfDB(String hostId) throws ClassNotFoundException {
        final File cfgFile = new File(DB_CFG_ROOT + hostId + DB_CFG_NAME);
        final Config conf = ConfigFactory.parseFile(cfgFile);

        final String driverName = getDbProp(conf, "driver");
        Class.forName(driverName);

        return Database.from(
            getDbProp(conf, "url"),
            getDbProp(conf, "username"),
            getDbProp(conf, "password")
        );
    }

    public static Database getDb() throws ClassNotFoundException {
        final String dbcfg = System.getProperty("dbcfg", "pg/epbygomw0024");
        log.info("dbcfg: {}", dbcfg);
        return getConfDB(dbcfg);
    }
}
