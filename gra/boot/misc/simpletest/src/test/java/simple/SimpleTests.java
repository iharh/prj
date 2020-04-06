package simple;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleTests {
    @Test
    void test1() throws Exception {
        assertThat(1).isNotEqualTo(0);
    }
}
