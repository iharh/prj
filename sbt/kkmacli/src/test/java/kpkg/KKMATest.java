package kpkg;

import org.junit.Test;
import org.junit.Ignore;


import org.snu.ids.ha.ma.MExpression;
import org.snu.ids.ha.ma.MorphemeAnalyzer;
import org.snu.ids.ha.ma.Sentence;
import org.snu.ids.ha.ma.Eojeol;
import org.snu.ids.ha.ma.Morpheme;
import org.snu.ids.ha.ma.Token;
import org.snu.ids.ha.ma.Tokenizer;

import org.snu.ids.ha.dic.Dictionary;

import org.snu.ids.ha.constants.POSTag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertThat;

//import static org.hamcrest.core.IsCollectionContaining.hasItem;
//import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

public class KKMATest {
    private static final Logger log = LoggerFactory.getLogger(KKMATest.class);

    private static final String [] TEXTS = { "🍇D", "🍇" };

    @Test
    public void testKKMA1() throws Exception {
        for (int i = 0; i < TEXTS.length; ++i) {
            String t = TEXTS[i];
            // logStringCodePoints(t);
            DumpUtils.dumpTokens(i, Tokenizer.tokenize(t));
        }
    }

    private void logStringCodePoints(String s) {
        log.info("text: {}", s);
        log.info("text_hex: {}", DumpUtils.hexBinUTF(s));

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
            log.info("tok_hex: {}", DumpUtils.hexBinUTF(tok));

            offset += Character.charCount(cp);
            log.info("next offset: {}", offset);
        }
    }
};
