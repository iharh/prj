package rxjdbc;

import com.github.davidmoten.rx.jdbc.Database;

import org.stringtemplate.v4.ST;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Paths;
import java.nio.file.Path;

import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;

import java.net.URI;

import java.util.Properties;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import java.nio.charset.Charset;

import static java.nio.charset.StandardCharsets.*;

public class PSUpdater {
    private static final Logger log = LoggerFactory.getLogger(PSUpdater.class);

    private static Database getDb(String drv, String url, String usr, String pwd) throws ClassNotFoundException {
        Class.forName(drv);
        log.info("connecting to drv: {} db url: {} usr: {}", drv, url, usr);
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

    private static String getDbType(String drv) {
        if (drv.indexOf("oracle") >= 0) {
            return "ora";
        } else if (drv.indexOf("postgresql") >= 0) {
            return "pg";
        } else {
            throw new IllegalArgumentException("can't detect db type for drv: " + drv);
        }
    }


    private static final int BUFFER_SIZE = 4 * 1024;

    private static String inputStreamToString(InputStream inputStream, Charset charset) throws IOException {
        StringBuilder builder = new StringBuilder();
        InputStreamReader reader = new InputStreamReader(inputStream, charset);
        char[] buffer = new char[BUFFER_SIZE];
        int length;
        while ((length = reader.read(buffer)) != -1) {
            builder.append(buffer, 0, length);
        }
        return builder.toString();
    }    

    public static void main(String [] args) throws Exception {
        log.info("start");
        final String propFileName = "../../server/conf/database.system.properties";

        try {
            Properties props = new Properties();
            props.load(new FileInputStream(propFileName));

            final String drv = props.getProperty("cmpDS.driver");
            final String url = props.getProperty("cmpDS.url");
            final String usr = props.getProperty("cmpDS.username");
            final String pwd = props.getProperty("cmpDS.password");

            final String dbType = getDbType(drv);
            final String updSqlFileName = "update-" + dbType + ".sql";

            final String resName = "/" + updSqlFileName;
            log.info("resName: {}", resName);


            //InputStream is = getClass().getResourceAsStream(resName);
            // Opens a resource from the current class' defining class loader
            InputStream istream = PSUpdater.class.getResourceAsStream(resName); // getClass()

            // Create a NIO ReadableByteChannel from the stream
            //ReadableByteChannel channel = Channels.newChannel(istream);

            final String updSQLTemplate = inputStreamToString(istream, UTF_8);
            /*new String(Files.readAllBytes(
                Paths.get(
                    PSUpdater.class.getClassLoader().getResource(resName).toURI()
                )
            ));*/
            //final String updSQLTemplate = new String(Files.readAllBytes(Paths.get(updSqlFileName)));

            final Database ssDB = getDb(drv, url, usr, pwd);

            final List<PSCred> psCreds = getPSCreds(ssDB);

            for (PSCred psCred : psCreds) {
                final Database psDB = getDb(drv, url, psCred.getUsr(), psCred.getPwd());

                ST st = new ST(updSQLTemplate, '$', '$');
                st.add("system_user_name", usr);
                final String updSQL = st.render();

                updateForSinglePS(psDB, updSQL);
            }

            log.info("finish - OK");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            System.exit(-1);
        }
    }
}
