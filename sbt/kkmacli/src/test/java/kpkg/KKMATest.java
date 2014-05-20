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

//import java.util.Set;
//import java.util.HashSet;
import java.util.List;
//import java.util.ArrayList;

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
            dumpTokens(i, Tokenizer.tokenize(TEXTS[i]));
        }
    }

    private void dumpTokens(int id, List<Token> tokens) {
        for (Token t : tokens) {
            assertNotNull(t);
            log.info("t" + id + ": {}", t.toString());
        }
    }
};
