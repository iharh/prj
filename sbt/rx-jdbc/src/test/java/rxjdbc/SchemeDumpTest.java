package rxjdbc;

import org.junit.Test;
//import org.junit.Ignore;

import static org.junit.Assert.assertNotNull;

import com.clarabridge.nlp.foundation.Scheme;
import com.clarabridge.nlp.foundation.TypeInfo;
import com.clarabridge.nlp.foundation.Attribute;
import com.clarabridge.nlp.foundation.NlpException;

import org.apache.commons.io.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;

public class SchemeDumpTest {
    private static final Logger log = LoggerFactory.getLogger(SchemeDumpTest.class);

    @Test
    public void testDump() throws Exception {
        String schemeFileName = "in/english_bb25_db.scheme";
        // "in/english_local.scheme";
        // "out/english.xml";
        // "d:/clb/inst/fx/en/scheme/english.scheme";
        FileInputStream schemeStream = new FileInputStream(schemeFileName);
        Scheme scheme = Scheme.load(schemeStream);
        assertNotNull(scheme);
        TypeInfo annTypeInfo = scheme.getType("Annotation");
        assertNotNull(annTypeInfo);
        Attribute numberAccessor = annTypeInfo.getAttribute("GrammarForm.Number");
        assertNotNull(numberAccessor);
        Long pluralCode = numberAccessor.getEnumCode("Plural"); // Plural
        assertNotNull(pluralCode);
    }
}
