package kpkg;

import org.snu.ids.ha.ma.Token;

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
        log.info("bytes: {}", DatatypeConverter.printHexBinary(t.getString().getBytes(UTF_8)));
    }
}
