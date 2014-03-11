package mpkg;

import mdm.in.InDoc;
import mdm.in.InSeg;

import mdm.out.OutDoc;
import mdm.out.OutSeg;
import mdm.out.Word;
import mdm.out.Analysis;
import mdm.out.MorphFS;
import mdm.out.MorphFSAttr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class MDMTestUtils {
    private static final String DOC1_ID = "ExampleDocument";
    private static final String DOC1_SEG1_ID = "SENT1";
    private static final String DOC1_SEG1_VAL = "السلطة الفلسطينية تستنكر استمرار سياسة الاغتيالات الاسرائي";

    private static final String PREPROC_VAL    = "AlslTp AlflsTynyp tstnkr AstmrAr syAsp AlAgtyAlAt AlAsrA}y";
    private static final String WORD0_WORD_VAL = "السلطة";
    private static final String WORD0_STEM_VAL = "سُلْط";

    public static InDoc getInDoc1() {
        InSeg [] segs = new InSeg[1];
        segs[0] = new InSeg(DOC1_SEG1_ID, DOC1_SEG1_VAL);
        return new InDoc(DOC1_ID, segs);
    }

    public static void checkOutDoc1(OutDoc outDoc) {
        assertNotNull(outDoc);
        assertEquals(DOC1_ID, outDoc.getId());

        OutSeg [] segs = outDoc.getSegs();
        assertNotNull(segs);
        assertThat(segs.length, greaterThan(0));

        int idx = 0;
        OutSeg outSeg = segs[idx];
        assertNotNull(outSeg);
        assertEquals(DOC1_SEG1_ID, outSeg.getId());

        Word [] words = outSeg.getWords();
        assertNotNull(words);
        assertThat(words.length, greaterThan(0));

        // Word 0
        Word word = words[idx];
        assertNotNull(word);

        assertEquals(idx, word.getId());
        assertEquals(WORD0_WORD_VAL, word.getWord());
        
        Analysis analysis = word.getAnalysis();
        assertNotNull(analysis);

        MorphFS morphFS = analysis.getMorphFS();
        assertNotNull(morphFS);

        MorphFSAttr [] attrs = morphFS.getAttrs();
        assertNotNull(attrs);
        assertThat(attrs.length, greaterThan(0));

        MorphFSAttr attr = attrs[0];
        assertNotNull(attr);

        assertThat(attrs, allOf(
            hasItemInArray(new MorphFSAttr("pos", "noun")),
            hasItemInArray(new MorphFSAttr("gen", "f")),
            hasItemInArray(new MorphFSAttr("num", "s"))
        ));

        assertEquals(0, word.getWordStartPos());
        assertEquals(6, word.getWordLength());

        // Word 1
        ++idx;
        word = words[idx];
        assertNotNull(word);
        assertEquals(7, word.getWordStartPos());
        assertEquals(10, word.getWordLength());

        // Word 2
        ++idx;
        word = words[idx];
        assertNotNull(word);
        assertEquals(18, word.getWordStartPos());
        assertEquals(6, word.getWordLength());

        // Word 3
        ++idx;
        word = words[idx];
        assertNotNull(word);
        assertEquals(25, word.getWordStartPos());
        assertEquals(7, word.getWordLength());
    }
}
