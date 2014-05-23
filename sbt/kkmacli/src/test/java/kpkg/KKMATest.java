package kpkg;

import org.junit.Test;
import org.junit.Ignore;


import org.snu.ids.ha.ma.MExpression;
import org.snu.ids.ha.ma.MorphemeAnalyzer;
import org.snu.ids.ha.ma.Sentence;
import org.snu.ids.ha.ma.Eojeol;
import org.snu.ids.ha.ma.Morpheme;
import org.snu.ids.ha.ma.Token;
//import org.snu.ids.ha.ma.Tokenizer;

import org.snu.ids.ha.dic.Dictionary;

import org.snu.ids.ha.constants.POSTag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import javax.xml.bind.DatatypeConverter;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertThat;

//import static org.hamcrest.core.IsCollectionContaining.hasItem;
//import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

import static java.nio.charset.StandardCharsets.*;

public class KKMATest {
    private static final Logger log = LoggerFactory.getLogger(KKMATest.class);

    private static final String [] TEXTS = { "🍇D", "🍇" };

    @Test
    public void testKKMA1() throws Exception {
        for (int i = 0; i < TEXTS.length; ++i) {
            String t = TEXTS[i];
            log.info("text: {}", DatatypeConverter.printHexBinary(t.getBytes(UTF_8)));
            //DumpUtils.dumpTokens(i, Tokenizer.tokenize(t));

            final int length = t.length();

            for (int offset = 0; offset < length; ) {
                final int codepoint = s.codePointAt(offset);
                // do something with the codepoint
                offset += Character.charCount(codepoint);
            }

            for (int j = 0; j < length; ++j) {
                log.info("ch: {}", t.charAt(j));
                log.info("codePoint: {}", Integer.toHexString(t.codePointAt(j)));
            }
        }
    }

};
