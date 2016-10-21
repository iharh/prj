package rxjdbc;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.io.FileUtils;
import com.github.davidmoten.rx.jdbc.Database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.io.IOException;

import java.util.List;

public class MappingTest {
    private static final Logger log = LoggerFactory.getLogger(MappingTest.class);

    private void dumpContent(Database db, String lang, String name) {
        db
            .select("select content from cb_file_repository where language_id=? and name=?")
            .parameters(lang, name)
            //.getAs(InputStream.class)
            .getAs(byte[].class)
            .subscribe(
                arr -> { // contentStream
                    try {
                        //FileUtils.copyInputStreamToFile(contentStream, new File("out/" + name + ".xml"));
                        FileUtils.writeByteArrayToFile(new File("out/" + name + ".xml"), arr);
                    } catch (IOException e) {
                        log.error("IO error:", e);
                    }
                }
            );
    }

    @Test
    public void testMapping() throws Exception {
        Database db = DBUtils.getDb();

        //update cb_lp set LP_VERSION=? where language_id=?
        dumpContent(db, "en", "english"); // config
        // ?? cb_shared_file_repository
    }
}
