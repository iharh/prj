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

    private static final String TEXT1 = "🍇D";
    private static final String TEXT2 = "🍇";
    @Test
    public void testKKMA1() throws Exception {
        List<Token> tokenList1 = Tokenizer.tokenize(TEXT1);
        //assertEquals(1, tokenList.size());
        for (Token t : tokenList1) {
            assertNotNull(t);
            log.info("t1: {}", t.toString());
        }
        List<Token> tokenList2 = Tokenizer.tokenize(TEXT2);
        //assertEquals(1, tokenList.size());
        for (Token t : tokenList2) {
            assertNotNull(t);
            log.info("t2: {}", t.toString());
        }
    }
};
