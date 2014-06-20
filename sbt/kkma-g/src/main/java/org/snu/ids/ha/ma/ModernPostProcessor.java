package org.snu.ids.ha.ma;

import org.snu.ids.ha.constants.Condition;
import org.snu.ids.ha.constants.POSTag;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.util.List;

public class ModernPostProcessor {
//    private static final Logger log = LoggerFactory.getLogger(ModernPostProcessor.class);

    public static void postProcess(List<MExpression> melAnalResult) throws Exception {
        MExpression me1 = null;
        MExpression me2 = null;
        MExpression me3 = null;
        int i;
        for (i = 1; i < melAnalResult.size(); i++)
        {
            me1 = (MExpression)melAnalResult.get(i - 1);
            me2 = (MExpression)melAnalResult.get(i);

            if (!me2.isComplete() || me1.isOneEojeolCheckable() || me2.isOneEojeolCheckable()) {
//                log.info("p1");
                if (me1.isNotHangul() && me2.isNotHangul()) {
//                    log.info("p11");
                    MCandidate mc1 = (MCandidate)me1.get(0);
                    MCandidate mc2 = (MCandidate)me2.get(0);
                    if (mc1.firstMorp.index + mc1.getExp().length() == mc2.firstMorp.index) {
//                        log.info("p111");
                        me1.exp = (new StringBuilder(String.valueOf(me1.exp))).append(me2.exp).toString();
                        mc1.addAll(mc2);
                        mc1.setExp(me1.exp);
                        melAnalResult.remove(i);
                        i--;
                    }
                } else if (me1.isNotHangul()) {
//                    log.info("p12");
                    MCandidate mc1 = (MCandidate)me1.get(0);
                    MCandidate mc2 = (MCandidate)me2.get(0);
                    if (mc1.firstMorp.index + mc1.getExp().length() == mc2.firstMorp.index) {
//                        log.info("p121");
                        me3 = me1.derive(me2);
                        melAnalResult.remove(i - 1);
                        melAnalResult.remove(i - 1);
                        melAnalResult.add(i - 1, me3);
                        i--;
                    }
                } else {
//                    log.info("p13");
                    me3 = me1.derive(me2);
                    if(me3.isOneEojeol()) {
//                        log.info("p131");
                        melAnalResult.remove(i - 1);
                        melAnalResult.remove(i - 1);
                        melAnalResult.add(i - 1, me3);
                        i--;
                    }
                }
            }
        }

        me1 = (MExpression)melAnalResult.get(0);
        i = 0;
        for (int size = 0; i < (size = me1.size()) && size > 1; i++) {
            MCandidate mc = (MCandidate)me1.get(i);
            if (mc.cclEnc != Condition.ENG && mc.cclEnc != 0L || mc.firstMorp.isTagOf(POSTag.J | POSTag.E | POSTag.XS)) {
                me1.remove(i);
                i--;
            }
        }

        setBestPrevMC(melAnalResult);
    }

    private static void setBestPrevMC(List<MExpression> meList) {
        MExpression mePrev = null, meCurr = null;
        for (int i = 0, size = meList.size(); i < size; i++) {
            meCurr = meList.get(i);
            meCurr.setBestPrevMC(mePrev);
            mePrev = meCurr;
        }

        int idx = meList.size() - 1;
        MExpression me = meList.get(idx);
        me.sortByBestLnpr(); // !!!
        MCandidate mc = me.get(0);
        for (idx--; mc != null && idx >= 0; idx--) {
            mc = mc.prevBestMC;
            me = meList.get(idx);
            me.remove(mc);
            me.add(0, mc);
        }
    }
};
