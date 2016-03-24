package rxjdbc;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.io.IOUtils;

import com.github.davidmoten.rx.jdbc.Database;
import com.github.davidmoten.rx.jdbc.tuple.Tuple2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.Reader;
import java.io.FileOutputStream;
import java.io.IOException;

import static java.nio.charset.StandardCharsets.*;

import java.util.List;

public class ComponentTest {
    private static final Logger log = LoggerFactory.getLogger(ComponentTest.class);

    private static FileOutputStream createOut(String name, Long id) throws IOException {
        return new FileOutputStream(new File("out/" + name + "-" + id.toString() + ".xml"));
    }

    private void dumpContent(Database db, String name) {
        log.info("start: {}", name);
        db
            .select("select id, cfg from cb_component where name like ?") // where name=?
            .parameters("%" + name + "%")
            .getAs(Long.class, Reader.class)
            .subscribe(
                res -> {
                    try {
                        Long id = res.value1();
                        Reader contentReader = res.value2();
                        log.info("found id: {}", id);
                        IOUtils.copy(contentReader, createOut(name, id), UTF_8);
                    } catch (IOException e) {
                        log.error("IO error:", e);
                    }
                }
            );
        log.info("finish");
    }

    @Test
    public void testMapping() throws Exception {
        Database db = DBUtils.getDb();

        dumpContent(db, "FXTransformer"); // FXTransformer
    }
}
