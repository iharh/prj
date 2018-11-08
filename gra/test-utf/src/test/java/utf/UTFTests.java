package utf;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import org.apache.commons.codec.binary.Hex;

import static java.nio.charset.StandardCharsets.*;

import static org.assertj.core.api.Assertions.assertThat;

public class UTFTests {
    private void dbg(String str) {
        if (true) {
            System.out.println(str);
        }
    }

    @Test
    public void test1() throws Exception {
        dbg(Hex.encodeHexString("123".getBytes(UTF_8)));
        assertThat(true).isEqualTo(true);
    }
}
