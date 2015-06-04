package rxjdbc;

import com.github.davidmoten.rx.jdbc.Database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.io.FileInputStream;

import java.util.Properties;
import java.util.List;

public class OraPSUpdater {
    private static final Logger log = LoggerFactory.getLogger(OraPSUpdater.class);

    private static Database getOraDb(String url, String usr, String pwd) throws ClassNotFoundException {
        Class.forName("oracle.jdbc.OracleDriver");
        log.info("connecting to db url:{} usr: {}", url, usr);
        return Database.from(url, usr, pwd);
    }

    public static class PSCred {
        private String usr;
        private String pwd;

        public PSCred(String usr, String pwd) {
            this.usr = usr;
            this.pwd = pwd;
        }

        public String getUsr() {
            return usr;
        }

        public String getPwd() {
            return pwd;
        }

        @Override
        public String toString() {
            return "usr: " + usr + " pwd: " + pwd;
        }
    };

    private static List<PSCred> getPSCreds(final Database ssDB) {
        List<PSCred> result = ssDB
            .select("select ds.username, ds.pwd from cb_datasource ds, cb_datflow_ds_xref dx where ds.id = dx.id_datasource and dx.id_conntype = 1")
            .autoMap(PSCred.class).toList().toBlocking().single();
        return result;
    }

    private static void updateForSinglePS(final Database psDB, final String updSQL) {
        Integer count = psDB
            .update(updSQL)
	    .count().first().toBlocking().single();
        log.debug("update cnt: {}", count.toString());
    }

    public static void main(String [] args) throws Exception {
        final String propFileName = "database.system.properties";
        final String updSqlFileName = "update.sql";

        try {
            Properties props = new Properties();
            props.load(new FileInputStream(propFileName));

            final String url = props.getProperty("cmpDS.url");
            final String usr = props.getProperty("cmpDS.username");
            final String pwd = props.getProperty("cmpDS.password");
            final String updSQL = new String(Files.readAllBytes(Paths.get(updSqlFileName)));

            final Database ssDB = getOraDb(url, usr, pwd);

            final List<PSCred> psCreds = getPSCreds(ssDB);

            for (PSCred psCred : psCreds) {
                final Database psDB = getOraDb(url, psCred.getUsr(), psCred.getPwd());
                updateForSinglePS(psDB, updSQL);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            System.exit(-1);
        }
    }
}
