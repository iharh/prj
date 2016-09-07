package kpkg;

import org.junit.Test;
import org.junit.Ignore;

import org.snu.ids.ha.core.MAnalyzerFull;
import org.snu.ids.ha.core.MCandidate;
import org.snu.ids.ha.core.MProcessor;
import org.snu.ids.ha.core.Sentence;
import org.snu.ids.ha.core.Tokenizer;
import org.snu.ids.ha.core.CharArray;

import org.snu.ids.ha.constants.POSTag;
import org.snu.ids.ha.constants.Condition;


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

    private static final String [] TEXTS = { "어제 유미가 그 남자를 만났어요." };
    //private static final String [] TEXTS = { "어제 유미가 그 남자를 만났어요. 어제 유미가 그 남자를 만났어요." };
    //private static final String [] TEXTS = { "나라마다 맥주 맛이 달라요." };
    //{ "($)", "ðŸ‡D", "ðŸ‡", "ë¥¼ ë§Œë‚¬.", "01:59", "324-35ë²ˆì§€", "7ì²œ" };

    private static boolean isApndblWithSpace(MCandidate mp, MCandidate mc) {
        return !mp.isLastTagOf(POSTag.V | POSTag.EP | POSTag.XP) && !mc.isFirstTagOf(POSTag.E | POSTag.XS | POSTag.VCP | POSTag.J) && !mc.isHavingCond(Condition.SHORTEN);
    }

    private static boolean isMergeable(MCandidate mp, MCandidate mc) {
        return mp.isApndbl(mc) && !isApndblWithSpace(mp, mc);
    }

    private static void mergeMC(MCandidate mp, MCandidate mc) {
        char [][] wordArr = mc.getWordArr();
        long [] infoEncArr = mc.getInfoEncArr();
        for (int idx = 0; idx < mc.size(); ++idx) {
            mp.addMorp(wordArr[idx], infoEncArr[idx]);
        }
        CharArray ca = new CharArray(mp.getArray(), mp.getStart(), mp.getLength() + mc.getLength());
        mp.setCharArray(ca);
    }

    private static void dumpMC(MCandidate mc) {
        log.info("");
        log.info("morph_str: {}", mc.getMorpStr());

        log.info("size: {}", mc.size());
        log.info("lastMorpIdx: {}", mc.getLastMorpIdx());

        log.info("start: {}", mc.getStart());
        log.info("length: {}", mc.getLength());
        log.info("exp: {}", mc.getExp());
    }

    @Test
    public void testDumpSent() throws Exception {
        MAnalyzerFull ma = new MAnalyzerFull();
        MProcessor mproc = new MProcessor();

        for (int i = 0; i < TEXTS.length; ++i) {
            String t = TEXTS[i];

            log.info("analizng text:\n{}", t);

            List<MCandidate> candidates = ma.analyze(t);
            List<Sentence> sentences = mproc.divide(candidates);

            for (Sentence sent : sentences) {
                log.info("sentence: {}", sent.toString());
                
                MCandidate mp = null;
                for (MCandidate mc : sent) {
                    if (mp != null) {
                        if (isMergeable(mp, mc)) {
                            /*mp =*/ mergeMC(mp, mc);
                            continue;
                        }
                        dumpMC(mp);
                    }

                    mp = mc;
                }
                // TODO: what about the tail ??
                dumpMC(mp);
            }
        }
    }

                    //log.info("str: {}", mc.toString());

                    //getTagAt(i) - return POSTag.getTag(getTagNumAt(idx)); - return infoEncArr[idx] & 0x7fffffffffffffffL;
                    //getTagNumAt(i)
                    //
                    //wordArr[i] - char [] getWordAt(idx) - String getStringAt(idx)
    //
    @Ignore
    public void testDumpCand() throws Exception {
        MAnalyzerFull ma = new MAnalyzerFull();

        for (int i = 0; i < TEXTS.length; ++i) {
            String t = TEXTS[i];

            log.info("analizng text:\n{}", t);

            List<MCandidate> candidates = ma.analyze(t);

            MCandidate mp = null;
            for (MCandidate mc : candidates) {
                log.info("");
                log.info("morph_str: {}", mc.getMorpStr());

                log.info("size: {}", mc.size());
                log.info("lastMorpIdx: {}", mc.getLastMorpIdx());

                log.info("start: {}", mc.getStart());
                log.info("length: {}", mc.getLength());
                log.info("exp: {}", mc.getExp());

                if (mp != null) {
                    log.info("previous mc is [{}mergeable] with this one", isMergeable(mp, mc) ? "" : "not ");
                }
                mp = mc;
            }
        }
    }

    @Ignore
    public void testDumpTokens() throws Exception {
        for (int i = 0; i < TEXTS.length; ++i) {
            String t = TEXTS[i];
            CharArray charArray = new CharArray(t);
            DumpUtils.dumpTokens(i, Tokenizer.tokenize(charArray));
        }
    }
};
