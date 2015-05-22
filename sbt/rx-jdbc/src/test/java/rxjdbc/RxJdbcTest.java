package rxjdbc;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;


import com.github.davidmoten.rx.jdbc.Database;
/*
import com.github.davidmoten.rx.jdbc.tuple.Tuple2;
import com.github.davidmoten.rx.jdbc.tuple.TupleN;
import com.github.davidmoten.rx.jdbc.tuple.Tuples;
*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class RxJdbcTest {
    private static final Logger log = LoggerFactory.getLogger(RxJdbcTest.class);

    private static Database getDb() throws ClassNotFoundException {
        final String driverClass = "oracle.jdbc.OracleDriver";
        //ClassLoader.getSystemClassLoader().loadClass(driverClass);
        Class.forName(driverClass);

        return Database.from("jdbc:oracle:thin:@//localhost:1521/ihorcl", "win_ss", "clb");
    }

    @Test
    public void testDb() throws Exception {
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
