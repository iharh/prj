package mypack;

import com.typesafe.config.ConfigFactory;
import com.typesafe.config.Config;

import org.postgresql.ds.PGSimpleDataSource;

import oracle.jdbc.pool.OracleDataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;
import java.io.IOException;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import static java.nio.charset.StandardCharsets.*;

public class Main {
    private static int CHECK_TOKENS_PORTION_SIZE = 1000;

    private static final String CHECK_TOKENS_PORTION = "select ms_token_name from p_ms_token where ms_token_name in ('%s')";


    private static Config getConf(String dsId) {
        return ConfigFactory.parseFile(new File("cfg/db-" + dsId + ".properties"));
    }

    private static BufferedReader getR(final String resName) {
        return new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/" + resName), UTF_8));
    }

    private static DataSource getPGDS(String url, String username, String password) throws SQLException {
        final PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setUrl(url);
        //ds.setDatabaseName(dbname);
        ds.setUser(username);
        ds.setPassword(password);
        return ds;
    }
    private static DataSource getOraDS(String url, String username, String password) throws SQLException {
        final OracleDataSource ds = new OracleDataSource();
        ds.setURL(url);
        //ds.setDatabaseName(dbname);
        ds.setUser(username);
        ds.setPassword(password);
        return ds;
    }

    private static DataSource getPGSysDS(String dsId) throws SQLException {
        final Config conf = getConf(dsId);
        return getPGDS(conf.getString("cmpDS.url"), conf.getString("cmpDS.username"), conf.getString("cmpDS.password"));
    }
    private static DataSource getPGPSDS(String dsId) throws SQLException {
        final Config conf = getConf(dsId);
        return getPGDS(conf.getString("cmpDS.url"), conf.getString("psDS.username"), conf.getString("psDS.password"));
    }
    private static DataSource getOraSysDS(String dsId) throws SQLException {
        final Config conf = getConf(dsId);
        return getOraDS(conf.getString("cmpDS.url"), conf.getString("cmpDS.username"), conf.getString("cmpDS.password"));
    }
    private static DataSource getOraPSDS(String dsId) throws SQLException {
        final Config conf = getConf(dsId);
        return getOraDS(conf.getString("cmpDS.url"), conf.getString("psDS.username"), conf.getString("psDS.password"));
    }

    private static JdbcTemplate getPGSysT(String dsId) throws SQLException {
        return new JdbcTemplate(getPGSysDS(dsId));
    }
    private static JdbcTemplate getPGPST(String dsId) throws SQLException {
        return new JdbcTemplate(getPGPSDS(dsId));
    }
    private static JdbcTemplate getOraSysT(String dsId) throws SQLException {
        return new JdbcTemplate(getOraSysDS(dsId));
    }
    private static JdbcTemplate getOraPST(String dsId) throws SQLException {
        return new JdbcTemplate(getOraPSDS(dsId));
    }


    private static void cntPrj(JdbcTemplate sysT) {
        Integer cnt = sysT.queryForObject("select count(*) from cb_project", Integer.class);
        System.out.println(String.format("cnt: %s", cnt));
    }

    private static void printDataSources(JdbcTemplate sysT) {
        final String sql = "select name, username, pwd, dbname from cb_datasource";
        sysT.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                String name = rs.getString(1);
                String username = rs.getString(2);
                String pwd = rs.getString(3);
                String dbname = rs.getString(4);
                System.out.println(String.format("name: %s, username: %s, pwd: %s, dbname: %s", name, username, pwd, dbname));
            }
        });
    }

    private static void processPortion(JdbcTemplate jdbcTemplate, Set<String> posnegWordsCache, List<String> portion) {
        final String sql = String.format(CHECK_TOKENS_PORTION, String.join("', '", portion));
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                String token = rs.getString(1);
                posnegWordsCache.remove(token);
            }
        });
    }

    private static void checkPosNeg(JdbcTemplate psT, List<String> posnegWords, Set<String> posnegWordsCache) {
        List<String> portion = new ArrayList<String>();
        for (String word: posnegWords) {
            portion.add(word);
            if (portion.size() >= CHECK_TOKENS_PORTION_SIZE) {
                processPortion(psT, posnegWordsCache, portion);
                portion.clear();
            }
        }
        processPortion(psT, posnegWordsCache, portion);
    }

    public static void main(final String[] args) {
        List<String> posnegWords = new ArrayList<String>();
        Set<String> posnegWordsCache = new HashSet<String>();
        // TODO: move to the cache
        try(
            final BufferedReader inR = getR("posneg.txt");
        ) {
            while (inR.ready()) {
                final String inS = inR.readLine();
                posnegWords.add(inS.replace("'", "''"));
                posnegWordsCache.add(inS);
            }
        } catch (IOException e) {
            System.err.println(e);
        }

        long beginMillis = System.currentTimeMillis();
        try {
            final JdbcTemplate psT = getPGPST("local"); // getOraPST("bart"), 
            checkPosNeg(psT, posnegWords, posnegWordsCache);
        } catch (SQLException e) {
            System.err.println(e);
        }
        long endMillis = System.currentTimeMillis();

        System.out.println(String.format("Total time: %d millis", (endMillis - beginMillis)));
    }
}
