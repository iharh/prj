package rxjdbc;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;


import com.github.davidmoten.rx.jdbc.Database;
import com.github.davidmoten.rx.jdbc.tuple.Tuple2;
/*
import com.github.davidmoten.rx.jdbc.tuple.TupleN;
import com.github.davidmoten.rx.jdbc.tuple.Tuples;
*/
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProjTest {
    private static final Logger log = LoggerFactory.getLogger(ProjTest.class);

    @Test
    public void testProj() throws Exception {
        Database db = DBUtils.getDbLocal();

        List<Tuple2<Integer, String>> projects = db
            .select("select id, name from cb_project")
            .getAs(Integer.class, String.class)
            .toList()
            .toBlocking()
            .single();

        log.info("start");
        for (Tuple2<Integer, String> p : projects) {
            log.info(p.value1() + " -> " + p.value2());
        }
        log.info("end");
// .select("select name from person where name > ? order by name")
// .parameter("ALEX")
        assertTrue(true);
    }
}
