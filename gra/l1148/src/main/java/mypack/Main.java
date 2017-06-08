package mypack;

import com.typesafe.config.ConfigFactory;
import com.typesafe.config.Config;

import org.postgresql.ds.PGSimpleDataSource;

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

import static java.nio.charset.StandardCharsets.*;

public class Main {
    private static int PORTION_SIZE = 1000;

    private static BufferedReader getR(final String resName) {
        return new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("/" + resName), UTF_8));
    }

    private static String getSqlForPortion(List<String> portion) {
        return "select ms_token_name from p_ms_token where ms_token_name in ('" + String.join("', '", portion) + "')";
    }

    private static void processPortion(JdbcTemplate jdbcTemplate, Set<String> posnegWordsCache, List<String> portion) {
        final String sql = getSqlForPortion(portion);
        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                String token = rs.getString(1);
                posnegWordsCache.remove(token);
            }
        });
    }

    public static void main(final String[] args) {
        List<String> posnegWords = new ArrayList<String>();
        Set<String> posnegWordsCache = new HashSet<String>();
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

        final Config conf = ConfigFactory.parseFile(new File("db.properties"));

        final String url = conf.getString("cmpDS.url");
        final String dbname = conf.getString("cmpDS.name");
        final String username = conf.getString("psDS.username");
        final String password = conf.getString("psDS.password");
        //System.out.println(String.format("Hello %s - %s", username, password));

        final PGSimpleDataSource ds = new PGSimpleDataSource();
        ds.setUrl(url);
        //ds.setDatabaseName(dbname);
        ds.setUser(username);
        ds.setPassword(password);

        final JdbcTemplate jdbcTemplate = new JdbcTemplate(ds);
        //Integer cnt = jdbcTemplate.queryForObject("select count(*) from p_ms_token", Integer.class);
        //System.out.println(String.format("cnt: %s", cnt));

        long beginMillis = System.currentTimeMillis();

        List<String> portion = new ArrayList<String>();
        for (String word: posnegWords) {
            portion.add(word);
            if (portion.size() >= PORTION_SIZE) {
                processPortion(jdbcTemplate, posnegWordsCache, portion);
                portion.clear();
            }
            //if (i % 1000 == 0) {
            //    System.out.println(i);
            //}
        }
        processPortion(jdbcTemplate, posnegWordsCache, portion);
        long endMillis = System.currentTimeMillis();

        System.out.println(String.format("Total time: %d millis", (endMillis - beginMillis)));
    }
}
