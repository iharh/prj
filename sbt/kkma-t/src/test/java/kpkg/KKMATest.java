package kpkg;

import org.junit.Test;
import org.junit.Ignore;

import org.snu.ids.ha.core.MAnalyzerFull;
import org.snu.ids.ha.core.MCandidate;
import org.snu.ids.ha.core.MProcessor;
import org.snu.ids.ha.core.Sentence;

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

    private static final String [] TEXTS = { "어제 유미가 그 남자를 만났어요. 어제 유미가 그 남자를 만났어요." };
    //{ "($)", "ðŸ‡D", "ðŸ‡", "ë¥¼ ë§Œë‚¬.", "01:59", "324-35ë²ˆì§€", "7ì²œ" };

    @Test
    public void testDumpSent() throws Exception {
        MAnalyzerFull ma = new MAnalyzerFull();
        MProcessor mp = new MProcessor();

        for (int i = 0; i < TEXTS.length; ++i) {
            String t = TEXTS[i];

            log.info("analizng text:\n{}", t);

            List<MCandidate> candidates = ma.analyze(t);
            List<Sentence> sentences = mp.divide(candidates);

            for (Sentence sent : sentences) {
                log.info("sentence: {}", sent.toString());
                for (MCandidate mc : sent) {
                    log.info("morph_str: {}", mc.getMorpStr());

                    log.info("size: {}", mc.size());
                    log.info("lastMorpIdx: {}", mc.getLastMorpIdx());

                    log.info("start: {}", mc.getStart());
                    log.info("length: {}", mc.getLength());
                    log.info("exp: {}", mc.getExp());

                    //log.info("str: {}", mc.toString());


                    //getTagAt(i) - return POSTag.getTag(getTagNumAt(idx)); - return infoEncArr[idx] & 0x7fffffffffffffffL;
                    //getTagNumAt(i)
                    //
                    //wordArr[i] - char [] getWordAt(idx) - String getStringAt(idx)
                }
            }
        }
    }
};
