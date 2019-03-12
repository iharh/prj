package utf;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import org.apache.commons.codec.binary.Hex;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.FileSystems;

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
        int num = 0;
        System.out.println("start");

        Path path = FileSystems.getDefault().getPath("/home/iharh/Downloads", "a.txt");
        try (BufferedReader br = Files.newBufferedReader(path, Charset.forName("windows-1251"))) {
            String line = null;
            do {
                line = br.readLine();
                ++num;
                System.out.println(num);
                if (line != null) {
                    System.out.println(line);
                }
            } while (line != null);
        }
        assertThat(num).isGreaterThan(0);
        // assertThat(num).isEqualTo(10241);
    }

    @Disabled
    public void test2() throws Exception {
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
