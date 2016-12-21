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
        final String schemeFileName = System.getProperty("schemeFile", "out/english.xml");
        log.info("schemeFileName: {}", schemeFileName);

        FileInputStream schemeStream = new FileInputStream(schemeFileName);
        Scheme scheme = Scheme.load(schemeStream);
        assertNotNull(scheme);

        checkTextSeg(scheme);
    }

    private void checkTextSeg(final Scheme scheme) throws Exception {
        TypeInfo typeInfo = scheme.getType("TextSegment"); // TextSegment Clause
        assertNotNull(typeInfo);
        Attribute attrAccessor = typeInfo.getAttribute("WordCount");
        assertNotNull(attrAccessor);
    }

    private void checkAnn(final Scheme scheme) throws Exception {
        TypeInfo annTypeInfo = scheme.getType("Annotation");
        assertNotNull(annTypeInfo);
        Attribute numberAccessor = annTypeInfo.getAttribute("GrammarForm.Number");
        assertNotNull(numberAccessor);
        Long pluralCode = numberAccessor.getEnumCode("Plural"); // Plural
        assertNotNull(pluralCode);
    }
}
