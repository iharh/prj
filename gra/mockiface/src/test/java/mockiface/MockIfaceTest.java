package mockiface;

import org.junit.Test;
//import org.junit.Ignore;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

//import lombok.extern.slf4j.Slf4j;

//@Slf4j
public class MockIfaceTest {

    @Test
    public void testMisc() throws Exception {
        //public interface SomeIface {
        //    void onSome(String arg);
        //}

        assertThat(true, is(true));
    }
}
