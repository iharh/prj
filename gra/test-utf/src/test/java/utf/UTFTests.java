package utf;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import static org.assertj.core.api.Assertions.assertThat;

public class UTFTests {
    private void dbg(String str) {
        if (true) {
            System.out.println(str);
        }
    }

    @Test
    public void test1() throws Exception {
        assertThat(true).isEqualTo(true);
    }
}
