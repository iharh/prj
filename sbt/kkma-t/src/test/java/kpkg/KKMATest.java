package kpkg;

import org.junit.Test;
import org.junit.Ignore;

import org.snu.ids.ha.core.MAnalyzerFull;
import org.snu.ids.ha.core.MCandidate;

//import org.snu.ids.ha.ma.ModernPostProcessor;
//import org.snu.ids.ha.ma.MExpression;
//import org.snu.ids.ha.ma.MorphemeAnalyzer;
//import org.snu.ids.ha.ma.Sentence;
//import org.snu.ids.ha.ma.Eojeol;
//import org.snu.ids.ha.ma.Morpheme;
//import org.snu.ids.ha.ma.Token;
//import org.snu.ids.ha.ma.Tokenizer;

//import org.snu.ids.ha.dic.Dictionary;

//import org.snu.ids.ha.constants.POSTag;

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

    private static final String [] TEXTS = { "324-35번지", "7천" };
    //{ "($)", "🍇D", "🍇", "를 만났.", "01:59", "324-35번지", "7천" };

    @Test
    public void testDumpSent() throws Exception {
        MAnalyzerFull ma = new MAnalyzerFull();

        for (int i = 0; i < TEXTS.length; ++i) {
            String t = TEXTS[i];

            log.info("analizng text:\n{}", t);

            List<MCandidate> rs = ma.analyze(t);
            for (int k = 0; k < rs.size(); ++k) {
                log.info("el: {}", rs.get(k).getMorpStr());
            }
/*
            // analyze morpheme without any post processing 
            List<MExpression> mexp = ma.analyze(t);

            log.info("after analyze:\n{}", mexp.toString());

            // refine spacing
            ModernPostProcessor.postProcess(mexp);

            log.info("after post process:\n{}", mexp.toString());

            // leave the best analyzed result
            mexp = ma.leaveJustBest(mexp);

            // divide result to setences
            List<Sentence> sentences = ma.divideToSentences(mexp);

            // print the result
            for (Sentence sentence : sentences) {
                String sentStr = sentence.getSentence();
                log.info("kkma sentence: {}", sentStr);
                log.info("");
                log.info("!!! eojeols !!!");
                log.info("");
                for (Eojeol eojeol : sentence) {
                    log.debug("eojeol: {}", eojeol.toString());
                }
            }
*/
        }
    }
};
