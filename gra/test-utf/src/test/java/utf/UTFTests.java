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
        String src = "1😌2";

        int i = 0;
        for (; i < src.length(); ++i) {
            int cp = src.codePointAt(i);
            if (Character.isBmpCodePoint(cp)) {
                dbg("char: " + (char)cp);
            } else if (Character.isValidCodePoint(cp)) {
                char [] chars = Character.toChars(cp);
                String str = new String(chars);
                dbg("surrogated: " + str);
                // Character.highSurrogate(cp), Character.lowSurrogate(cp)
                ++i;
            } else {
                dbg("invalid");
            }
        }
        // dbg(Hex.encodeHexString("123".getBytes(UTF_8)));
        assertThat(true).isEqualTo(true);
    }
}
