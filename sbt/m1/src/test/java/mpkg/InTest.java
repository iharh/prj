package mpkg;

import mdm.MDMConverter;

import mdm.in.InDoc;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;


import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;

import static java.nio.charset.StandardCharsets.*;

public class InTest {
    private static final String OUT_FILE = "mdm_in.xml";

    @Ignore
    public void testPrepareInput() throws Exception {
        MDMConverter.marshal(MDMTestUtils.getInDoc1(),
            new BufferedWriter(
                new OutputStreamWriter(
                    new FileOutputStream(OUT_FILE), UTF_8
                )
            )
        );
        assertTrue(true);
    }
};
