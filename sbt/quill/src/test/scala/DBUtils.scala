import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config

//import com.github.davidmoten.rx.jdbc.Database

import org.apache.commons.lang3.SystemUtils

import java.io.File

import org.slf4j.Logger
import org.slf4j.LoggerFactory

object DBUtils {
    private val log = LoggerFactory.getLogger(this.getClass.getName)

    private val DB_CFG_ROOT = if (SystemUtils.IS_OS_LINUX)
        "/data/wrk/clb/hosts/db/" else
        "D:\\dev\\notes\\wrk\\clb\\hosts\\db\\"
    private val DB_CFG_SUFFIX = ".database.system.properties"

    private def getDBPropFile(hostId: String, dbId: String): File = {
        val cfgFileName = DB_CFG_ROOT + "ora" + File.separator + hostId + DB_CFG_SUFFIX
        //log.info("cfgFileName: {}", cfgFileName)
        new File(cfgFileName)
    }

    private def getOraPropFile(hostId: String): File = getDBPropFile(hostId, "ora")
    private def getPGPropFile(hostId: String): File = getDBPropFile(hostId, "pg")

    private def getOraConf(hostId: String): Config = ConfigFactory.parseFile(getOraPropFile(hostId))
    private def getPGConf(hostId: String): Config = ConfigFactory.parseFile(getPGPropFile(hostId))
/*
    // throws ClassNotFoundException
    public static Database getPgDb(String url, String usr, String pwd) throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        return Database.from(url, usr, pwd);
    }

    private def getOraDb(url: String, usr: String, pwd: String): Database = {
        Class.forName("oracle.jdbc.OracleDriver")
        Database.from(url, usr, pwd)
    }

    private def getPGDb(url: String, usr: String, pwd: String): Database = {
        Class.forName("oracle.jdbc.???PGDriver")
        Database.from(url, usr, pwd)
    }
*/
    private def getDbProp(conf: Config, propName: String): String = {
        val result = conf.getString("cmpDS." + propName)
        log.debug(propName + ": {}", result)
        result;
    }
/*
    //throws ClassNotFoundException 

    def getOraConfDB(hostId: String): Database = {
        val conf = getOraConf(hostId);
        getOraDb(
            getDbProp(conf, "url"),
            getDbProp(conf, "username"),
            getDbProp(conf, "password")
        )
    }

    // throws ClassNotFoundException
    def getDb(): Database = 
        getOraConfDB("epbygomw0024")
        //getOraConfDB("tangerine6");
        //getPgConfDB("epbygomw0024");
        //getOraConfDB("qa10");
*/
}
