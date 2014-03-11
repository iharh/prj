package mpkg;

import mdm.MDMConverter;

import java.io.BufferedReader;
import java.io.InputStreamReader;


import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;


import static java.nio.charset.StandardCharsets.*;

public class OutTest {
    private static final String RES_OUT_XML = "sample_mdm_out.xml";

    @Ignore
    public void testAnalyzeOutput() throws Exception {
        MDMTestUtils.checkOutDoc1(
            MDMConverter.unmarshal(
                new BufferedReader(
                    new InputStreamReader(
                        getClass().getResourceAsStream("/" + RES_OUT_XML),
                        UTF_8
                    )
                )
            )
        );
        assertTrue(true);
    }
};
