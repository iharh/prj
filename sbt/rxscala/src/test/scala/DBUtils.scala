import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config

import com.github.davidmoten.rx.jdbc.Database

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

    private def getOraPropFile(hostId: String): File = {
        // File.separator
        val cfgFileName = DB_CFG_ROOT + "ora" + File.separator + hostId + DB_CFG_SUFFIX
        //log.info("cfgFileName: {}", cfgFileName)
        new File(cfgFileName)
    }

//    private static File getPgPropFile(String hostId) {
//        return new File(DB_CFG_ROOT + "pg\\" + hostId + DB_CFG_SUFFIX);
//    }

    private def getOraConf(hostId: String): Config = 
        ConfigFactory.parseFile(getOraPropFile(hostId))

//    private static Config getPgConf(String hostId) {
//        return ConfigFactory.parseFile(getPgPropFile(hostId));
//    }

//    public static Database getPgDb(String url, String usr, String pwd) throws ClassNotFoundException {
//        Class.forName("org.postgresql.Driver");
//        return Database.from(url, usr, pwd);
//    }

    // throws ClassNotFoundException
    private def getOraDb(url: String, usr: String, pwd: String): Database = {
        Class.forName("oracle.jdbc.OracleDriver")
        Database.from(url, usr, pwd)
    }

    private def getDbProp(conf: Config, propName: String): String = {
        val result = conf.getString("cmpDS." + propName)
        log.debug(propName + ": {}", result)
        result;
    }

    //throws ClassNotFoundException 
    def getOraConfDB(hostId: String): Database = {
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
    def getDb(): Database = 
        getOraConfDB("epbygomw0024")
        //getOraConfDB("tangerine6");
        //getPgConfDB("epbygomw0024");
        //getOraConfDB("qa10");

    def getOraProcessingDB(hostId: String, prj: String): Database = {
        val conf = getOraConf(hostId);
        val url = getDbProp(conf, "url")
        val db = getOraDb(
            url,
            getDbProp(conf, "username"),
            getDbProp(conf, "password")
        )
        def sqlQuery = s"""select
    ds.username, ds.pwd
from
    cb_project pj,
    cb_datflow_ds_xref dx,
    cb_datasource ds
where
    pj.name = '$prj'
    and pj.id = dx.project_id
    and dx.id_datasource = ds.id
    and dx.id_conntype = 1"""

        val p = db
            .select(sqlQuery)
            .getAs(classOf[String], classOf[String])
            .take(1)
            .toBlocking()
            .single()
        val login = p.value1()
        val pwd = p.value2()
        //log.info("getOraProcessingDB login: {}, pwd: {}", login, pwd:Any) 
        getOraDb(url, login, pwd)
    }
}
