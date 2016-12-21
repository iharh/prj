package rxjdbc;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;


import com.github.davidmoten.rx.jdbc.Database;
import com.github.davidmoten.rx.jdbc.tuple.Tuple4;
//import com.github.davidmoten.rx.jdbc.tuple.TupleN;
//import com.github.davidmoten.rx.jdbc.tuple.Tuples;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import java.util.List;

public class FileRepoListTest {
    private static final Logger log = LoggerFactory.getLogger(FileRepoListTest.class);

    @Test
    public void testFileRepoList() throws Exception {
        Database db = DBUtils.getDb();

        List<Tuple4<String, String, String, BigDecimal>> values = db
            .select("select id, type, kind, version_id from cb_file_repository where language_id='en' and name='english'")
            .getAs(String.class, String.class, String.class, BigDecimal.class)
            .toList()
            .toBlocking()
            .single();

        for (Tuple4<String, String, String, BigDecimal> p : values) {
            log.info("id: {} type: {} kind: {} version_id: {}", p.value1(), p.value2(), p.value3(), p.value4());
        }
        assertTrue(true);
    }
}
