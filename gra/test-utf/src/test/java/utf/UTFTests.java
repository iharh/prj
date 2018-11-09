package utf;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import org.apache.commons.codec.binary.Hex;

import static java.nio.charset.StandardCharsets.*;

import static org.assertj.core.api.Assertions.assertThat;

public class UTFTests {
    private static void dbg(String str) {
        if (true) {
            System.out.println(str);
        }
    }

    @Test
    public void test1() throws Exception {
        "1😌2"
            .codePoints()
            .forEach(cp -> {
                if (Character.isBmpCodePoint(cp)) {
                    dbg("char: " + (char)cp);
                } else if (Character.isValidCodePoint(cp)) {
                    char [] chars = Character.toChars(cp);
                    String str = new String(chars);
                    dbg("surrogated: " + str);
                    // Character.highSurrogate(cp), Character.lowSurrogate(cp)
                } else {
                    dbg("invalid");
                }
            });
        // dbg(Hex.encodeHexString("123".getBytes(UTF_8)));
        assertThat(true).isEqualTo(true);
    }
}
