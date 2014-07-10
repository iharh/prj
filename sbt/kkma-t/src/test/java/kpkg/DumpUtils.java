package kpkg;

import org.snu.ids.ha.core.Token;
import org.snu.ids.ha.core.CharArray;

import java.util.List;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.nio.charset.StandardCharsets.*;

public class DumpUtils {
    private static final Logger log = LoggerFactory.getLogger(DumpUtils.class);
/*
    private static String bytesToHex(byte[] in) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : in) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
*/
    public static void dumpTokens(int id, List<Token> tokens) {
        for (Token t : tokens) {
            //assertNotNull(t);
            dumpToken(id, t);
        }
    }

    public static void dumpToken(int id, Token t) {
        log.info("t" + id + ": {}", t.toString());
        String s = new String(t.getWord());
        log.info("t" + id + " bytes: {}", hexBinUTF(s));
    }

    public static String hexBinUTF(String s) {
        return DatatypeConverter.printHexBinary(s.getBytes(UTF_8));
    }

    public static void dumpStringCodePoints(String s) {
        log.info("text: {}", s);
        log.info("text_hex: {}", hexBinUTF(s));

        final int length = s.length();

        for (int offset = 0; offset < length; ) {
            final int cp = s.codePointAt(offset);
            log.info("cur offset: {}", offset);
            log.info("codepoint: {}", Integer.toHexString(cp));

            String tok = new String(Character.toChars(cp));
                    // String.valueOf(Character.toChars(cp)));
                    // new String(codeUnits, 0, count));
                    // new StringBuilder().appendCodePoint(cp).toString());
                    // String.format("%c", cp);

            log.info("tok: {}", tok);
            log.info("tok_hex: {}", hexBinUTF(tok));

            offset += Character.charCount(cp);
            log.info("next offset: {}", offset);
        }
    }

    public static void dumpUnicodeBlocks(String s) {
        for (int i = 0; i < s.length(); ++i) {
            char ch = s.charAt(i);
            Character.UnicodeBlock ub = Character.UnicodeBlock.of(ch);
            log.info("ch" + i + " : {}", ch);
            log.info("ub" + i + " : {}", ub);
        }
    }
}
