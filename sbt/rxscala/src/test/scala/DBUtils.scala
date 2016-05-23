import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config

import com.github.davidmoten.rx.jdbc.Database

import java.io.File

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object DBUtils {
    private val log = LoggerFactory.getLogger(this.getClass.getName)

    private val DB_CFG_ROOT = "D:\\dev\\notes\\wrk\\clb\\hosts\\db\\"
    private val DB_CFG_NAME = ".database.system.properties"

    private def getOraPropFile(hostId : String) : File =
        new File(DB_CFG_ROOT + "ora\\" + hostId + DB_CFG_NAME)

//    private static File getPgPropFile(String hostId) {
//        return new File(DB_CFG_ROOT + "pg\\" + hostId + DB_CFG_NAME);
//    }

    private def getOraConf(hostId : String) : Config = 
        ConfigFactory.parseFile(getOraPropFile(hostId))

//    private static Config getPgConf(String hostId) {
//        return ConfigFactory.parseFile(getPgPropFile(hostId));
//    }

//    public static Database getPgDb(String url, String usr, String pwd) throws ClassNotFoundException {
//        Class.forName("org.postgresql.Driver");
//        return Database.from(url, usr, pwd);
//    }

    // throws ClassNotFoundException
    private def getOraDb(url : String, usr : String, pwd : String) : Database = {
        Class.forName("oracle.jdbc.OracleDriver")
        Database.from(url, usr, pwd)
    }

    private def getDbProp(conf : Config, propName : String) : String = {
        val result = conf.getString("cmpDS." + propName)
        log.debug(propName + ": {}", result)
        result;
    }

    //throws ClassNotFoundException 
    private def getOraConfDB(hostId : String) : Database = {
        val conf = getOraConf(hostId);
        getOraDb(
            getDbProp(conf, "url"),
            getDbProp(conf, "username"),
            getDbProp(conf, "password")
        )
    }
/*
    public static Database getPgConfDB(String hostId) throws ClassNotFoundException {
        final Config conf = getPgConf(hostId);
        return getPgDb(
            getDbProp(conf, "url"),
            getDbProp(conf, "username"),
            getDbProp(conf, "password")
        );
    }
*/
    // throws ClassNotFoundException
    def getDb() : Database = 
        getOraConfDB("epbygomw0024")
        //getOraConfDB("tangerine6");
        //getPgConfDB("epbygomw0024");
        //getOraConfDB("qa10");
}
