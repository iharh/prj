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

import static java.nio.charset.StandardCharsets.*;

public class RuleItemTest {
    private static final Logger log = LoggerFactory.getLogger(RuleItemTest.class);

    @Test
    public void testRuleItems() throws Exception {
        Database db = DBUtils.getDbLocal();

        List<Tuple2<Integer, String>> ruleItems = db
            .select("select id, value from cb_ruleitem")
            .getAs(Integer.class, String.class)
            .toList()
            .toBlocking()
            .single();

        log.info("start");

        Path path = FileSystems.getDefault().getPath(".", "out.txt");
        try (BufferedWriter wr = Files.newBufferedWriter(path, UTF_8, StandardOpenOption.CREATE)) { // Charset.defaultCharset()
            for (Tuple2<Integer, String> i : ruleItems) {
                String ruleItem = i.value2();
                log.info(i.value1() + " -> " + ruleItem);
                wr.write(ruleItem);
            }
        }
        log.info("end");

        assertTrue(true);
    }
}
