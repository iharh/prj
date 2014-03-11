package mpkg;

import mdm.avail.AvailChecker;

import org.junit.Test;
import org.junit.Ignore;

//import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.is;

import java.util.concurrent.TimeUnit;

public class AvailCheckerTest {


    @Test
    public void test() throws Exception {
        AvailChecker checker = new AvailChecker(250);

        assertTrue(checker.isAvailable());
        checker.signallError();
        assertThat(checker.isAvailable(), is(false));
        TimeUnit.MILLISECONDS.sleep(250);
        assertThat(checker.isAvailable(), is(true));
    }
}

