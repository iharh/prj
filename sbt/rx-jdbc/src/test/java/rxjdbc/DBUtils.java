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

    private static File getOraPropFile(String hostId) {
        return new File(DB_CFG_ROOT + "ora\\" + hostId + DB_CFG_NAME);
    }

    private static File getPgPropFile(String hostId) {
        return new File(DB_CFG_ROOT + "pg\\" + hostId + DB_CFG_NAME);
    }

    private static Config getOraConf(String hostId) {
        return ConfigFactory.parseFile(getOraPropFile(hostId));
    }

    private static Config getPgConf(String hostId) {
        return ConfigFactory.parseFile(getPgPropFile(hostId));
    }

    private static Database getPgDb(String url, String usr, String pwd) throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        return Database.from(url, usr, pwd);
    }

    private static Database getOraDb(String url, String usr, String pwd) throws ClassNotFoundException {
        Class.forName("oracle.jdbc.OracleDriver");
        return Database.from(url, usr, pwd);
    }

    private static String getDbProp(final Config conf, String propName) {
        final String result = conf.getString("cmpDS." + propName);
        log.debug(propName + ": {}", result);
        return result;
    }

    public static Database getOraConfDB(String hostId) throws ClassNotFoundException {
        final Config conf = getOraConf(hostId);
        return getOraDb(
            getDbProp(conf, "url"),
            getDbProp(conf, "username"),
            getDbProp(conf, "password")
        );
    }
    
    public static Database getDb() throws ClassNotFoundException {
        //return getOraConfDB("epbygomw0024");
        return getOraConfDB("qa10");
    }
}
