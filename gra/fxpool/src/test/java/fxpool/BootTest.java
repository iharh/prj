package fxpool;

import org.junit.ClassRule;
import org.junit.Test;

import feign.Response;

import static org.assertj.core.api.Assertions.assertThat;
//import static org.rnorth.visibleassertions.VisibleAssertions.assertTrue;

public class BootTest {
    // http://junit.org/junit4/javadoc/4.12/org/junit/ClassRule.html
    // TODO: before()/after() ?
    @ClassRule
    public static JavaTestContainer javaTC = new JavaTestContainer();

    @Test
    public void simpleRecursiveFileTest() throws Exception {
        Response response = javaTC.getClient().getHello();

        System.out.println("Got feign response:\n" + response);

        assertThat(response.status()).isEqualTo(200);

        /*assertThat(response.headers().get("X-My-Super-Header"))
                .isNotNull()
                .hasSize(1)
                .containsExactly("header value");
                */

        //assertTrue("This should be true", true);
    }
}
