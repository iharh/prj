package rxjdbc;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import com.github.davidmoten.rx.jdbc.Database;
import com.github.davidmoten.rx.jdbc.tuple.Tuple2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;

import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import java.util.List;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.*;

public class RuleItemTest {
    private static final Logger log = LoggerFactory.getLogger(RuleItemTest.class);

    @Test
    public void testRuleItems() throws Exception {
        Database db = DBUtils.getDb();

        log.info("start");

        //final String sql = "select id, value from cb_ruleitem";
        //final int prjId = 8452;
        final int prjId = 8516782;
        final String sql = "select id, value from cb_ruleitem where id_rule in (select id from cb_rule where id_model in (select id from cb_class_model where id_project = " + prjId + "))";

        /*List<Tuple2<Integer, String>> ruleItems = db
            .select("select id, value from cb_ruleitem")
            .getAs(Integer.class, String.class)
            .toList()
            .toBlocking()
            .single();*/

        Path path = FileSystems.getDefault().getPath(".", "out.txt");
        try (BufferedWriter wr = Files.newBufferedWriter(path, UTF_8, StandardOpenOption.CREATE)) { // Charset.defaultCharset()
            db
                .select(sql)
                .getAs(Integer.class, String.class)
                .subscribe((Tuple2<Integer, String> i) -> {
                    try {
                        String ruleItem = i.value2();
                        log.info(i.value1() + " -> " + ruleItem);
                        wr.write(ruleItem);
                        wr.newLine();
                    } catch (IOException e) {
                        log.error(e.getMessage(), e);
                    }
                });
        }
        log.info("end");

        assertTrue(true);
    }
}
