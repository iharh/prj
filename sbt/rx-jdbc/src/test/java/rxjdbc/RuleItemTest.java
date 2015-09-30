package rxjdbc;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import com.github.davidmoten.rx.jdbc.Database;
import com.github.davidmoten.rx.jdbc.tuple.Tuple2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

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
        for (Tuple2<Integer, String> i : ruleItems) {
            log.info(i.value1() + " -> " + i.value2());
        }
        log.info("end");

        assertTrue(true);
    }
}
