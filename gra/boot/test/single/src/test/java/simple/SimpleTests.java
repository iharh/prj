package simple;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleTests {

    @Test
    void testSimple() throws Exception {
        assertThat(true).isEqualTo(true);
    }
}
