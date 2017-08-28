package fxpool;

import org.junit.ClassRule;
import org.junit.Test;

// https://github.com/OpenFeign/feign/blob/master/core/src/main/java/feign/Response.java
import feign.Response;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.StringWriter;

import java.nio.charset.Charset;

import java.util.ArrayList;
import java.util.List;

// https://github.com/joel-costigliola/assertj-core
import static org.assertj.core.api.Assertions.assertThat;

public class BootTest {
    // http://junit.org/junit4/javadoc/4.12/org/junit/ClassRule.html
    // TODO: before()/after() ?
    @ClassRule
    public static JavaTestContainer javaTC = new JavaTestContainer();

    @Test
    public void simpleRecursiveFileTest() throws Exception {
        Response response = javaTC.getClient().getHello();

        // System.out.println("Got feign response:\n" + response);

        assertThat(response.status()).isEqualTo(200);

        assertThat(loadContents(response.body().asInputStream()))
            .isNotNull()
            .startsWith("os.name: ")
        ;
    }

    // https://github.com/joel-costigliola/assertj-core/blob/master/src/main/java/org/assertj/core/internal/Diff.java
    // https://github.com/joel-costigliola/assertj-core/blob/master/src/main/java/org/assertj/core/api/Assertions.java
    //  contentOf -> Files.contentOf
    // https://github.com/joel-costigliola/assertj-core/blob/master/src/main/java/org/assertj/core/util/Files.java
    //  loadContents, loadLines
    // https://github.com/joel-costigliola/assertj-core/blob/master/src/main/java/org/assertj/core/util/URLs.java
    //  loadContents

    private List<String> linesFromBufferedReader(BufferedReader reader) throws IOException {
        String line;
        List<String> lines = new ArrayList<>();
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        return lines;
    }

    private static String loadContents(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, Charset.defaultCharset()));
        boolean threw = true;
        try {
            StringWriter writer = new StringWriter();
            int c;
            while ((c = reader.read()) != -1) {
                writer.write(c);
            }
            threw = false;
            return writer.toString();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                if (!threw) {
                    throw e; // if there was an initial exception, don't hide it
                }
            }
        }
    }
}
