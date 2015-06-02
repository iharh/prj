package base64;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.elasticsearch.common.Base64;

import com.google.protobuf.CodedInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Formatter;

import java.io.IOException;

public class BaseDecodeTest {
    private static final Logger log = LoggerFactory.getLogger(BaseDecodeTest.class);

    @Test
    public void testDecode() throws Exception {
        doDecode("+Ky8QgYBBwA=");
        doDecode("vb68QgYBBwA=");

        /*
        byte [] b = new byte [3];
        b[0] = (byte)0x1;
        b[1] = (byte)0xC;
        b[2] = (byte)0xFF;
        final String hex = bytesToStr(b);
        log.info("b: {}", hex);
        */
        assertTrue(true);
    }

    private static void doDecode(String encoded) throws IOException {
        byte [] data = Base64.decode(encoded);
        log.info("decode: {}", encoded);

        final String hex = bytesToStr(data);
        log.info("val: {}", hex);

        CodedInputStream in = CodedInputStream.newInstance(data);
            
        long baseTokenId = 0;
        long tokenId = in.readInt64() + baseTokenId;
        log.info("tokenId: {}", tokenId);
    }

    private static String bytesToStr(byte [] bytes) {
        Formatter formatter = new Formatter();
        for (byte b : bytes) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
}

