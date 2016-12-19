package rxjdbc;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;


import com.github.davidmoten.rx.jdbc.Database;
import com.github.davidmoten.rx.jdbc.tuple.Tuple3;
//import com.github.davidmoten.rx.jdbc.tuple.TupleN;
//import com.github.davidmoten.rx.jdbc.tuple.Tuples;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import java.util.List;

public class FileRepoListTest {
    private static final Logger log = LoggerFactory.getLogger(ProjTest.class);

    @Test
    public void testFileRepoList() throws Exception {
        Database db = DBUtils.getDb();

        List<Tuple3<String, String, BigDecimal>> values = db
            .select("select name, kind, version_id from cb_file_repository where language_id='en'") // id, name, type, kind, version_id
            .getAs(String.class, String.class, BigDecimal.class)
            .toList()
            .toBlocking()
            .single();

        log.info("start");
        for (Tuple3<String, String, BigDecimal> p : values) {
            log.info("{} -> {} -> {}", p.value1(), p.value2(), p.value3());
        }
        log.info("end");
        assertTrue(true);
    }
}
