package rxjdbc;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;


import com.github.davidmoten.rx.jdbc.Database;

import com.github.davidmoten.rx.jdbc.tuple.Tuple3;
/*
import com.github.davidmoten.rx.jdbc.tuple.Tuple2;
import com.github.davidmoten.rx.jdbc.tuple.TupleN;
import com.github.davidmoten.rx.jdbc.tuple.Tuples;
*/

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class StagingTest {
    private static final Logger log = LoggerFactory.getLogger(StagingTest.class);

    private static Database getPgDB(String url, String usr, String pwd) throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
        return Database.from(url, usr, pwd);
    }

    private static Database getDb() throws ClassNotFoundException {
        //return Database.from("jdbc:oracle:thin:@//localhost:1521/ihorcl", "win_ss", "clb");
        //return getPgDB("jdbc:postgresql://epbygomw0024:5432/postgres", "win_ss", "clb");
        return getPgDB("jdbc:postgresql://epbygomw0024:5432/postgres", "nav_st_en1", "Y3a#K2D#sy#N4#b0W#fL9n2S$");
    }

    private static int dumpToFile(int i, String verb) throws IOException, UnsupportedEncodingException {
        if (verb == null || verb.length() == 0) {
            return i;
        }
        
        File file = new File("d:/-/verb" + i + ".txt");
        Files.write(Paths.get(file.toURI()),
            verb.getBytes("utf-8"), // TODO: use predefined
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        );
        
        return i + 1;
    }

    @Test
    public void testSTDLExport() throws Exception {
        Database db = getDb();

        List<Tuple3<String, String, String>> verbs = db
            .select("select user_post, comment_text, page_post from staging_dataloader")
            .getAs(String.class, String.class, String.class)
            .toList()
            .toBlocking()
            .single();

        int i = 0;
        for (Tuple3<String, String, String> v : verbs) {
            //log.info("{}, {}, {}", v.value1(), v.value2(), v.value3());
            i = dumpToFile(i, v.value1());
            i = dumpToFile(i, v.value2());
            i = dumpToFile(i, v.value3());
        }
        assertTrue(true);
    }

    @Ignore
    public void testSTDLCnt() throws Exception {
        Database db = getDb();

        Integer project_cnt = db
            .select("select count (*) from staging_dataloader")
            .getAs(Integer.class)
            .toBlocking()
            .single();

        log.info("staging_dl cnt: {}", project_cnt);
        assertTrue(true);
    }

    @Ignore
    public void testProjectsCnt() throws Exception {
        Database db = getDb();

        Integer project_cnt = db
            .select("select count (*) from cb_project")
            .getAs(Integer.class)
            .toBlocking()
            .single();

        log.info("project cnt: {}", project_cnt);
        assertTrue(true);
    }

    @Ignore
    public void testProjects() throws Exception {
        Database db = getDb();

        List<String> project_names = db
            .select("select name from cb_project")
            .getAs(String.class)
            .toList()
            .toBlocking()
            .single();

        log.info("start");
        for (String p : project_names) {
            log.info(p);
        }
        log.info("end");
// .select("select name from person where name > ? order by name")
// .parameter("ALEX")
        assertTrue(true);
    }
}
