package mpkg;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.Reader;
import java.io.IOException;

import java.util.Map;
import java.util.Properties;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class MappingTest {
    private static final Logger log = LoggerFactory.getLogger(MappingTest.class);

    private static final String RES_MAPPING_PROPERTIES = "morph_ar_mapping.properties";

    private static final String PROP_ASP_C = "asp.c";
    private static final String PROP_ASP_C_VAL = "GrammarForm.Aspect: \"Command\"";

    private static final String OUT_FILE_PROPERTIES = "list.properties";
    private static final String OUT_FILE_PARESE_LOG = "parse.log";

    @Ignore
    public void testMapping() throws Exception {
        Properties mappingProps = new Properties();
        try(
            InputStream is = getClass().getResourceAsStream("/" + RES_MAPPING_PROPERTIES);
        ) {
            mappingProps.load(is);
            for (String propKey : mappingProps.stringPropertyNames()) {
                log.debug("mapping key: {}", propKey);
                String propVal = mappingProps.getProperty(propKey);
                log.debug("  mapping val: {}", propVal);

                Splitter kvSplitter = Splitter.on(':').omitEmptyStrings().trimResults();
                Map<String, String> mapSplitter = Splitter.on(',').trimResults().omitEmptyStrings().withKeyValueSeparator(kvSplitter).split(propVal);
                for (Map.Entry<String, String> e : mapSplitter.entrySet()) {
                    log.debug("    attr name: {}", e.getKey());
                    String attrVal = CharMatcher.is('\"').trimFrom(e.getValue());
                    log.debug("    attr tval: {}", attrVal);
                }
            }
        }
        String propVal = mappingProps.getProperty(PROP_ASP_C);
        assertEquals(PROP_ASP_C_VAL, propVal);
    }
};
