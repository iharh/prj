package mdm.xstream;

import mdm.out.OutDoc;
import mdm.out.OutSeg;
import mdm.out.Word;
import mdm.out.Analysis;
import mdm.out.MorphFS;

import mdm.xstream.out.MorphFSConverter;

import com.thoughtworks.xstream.XStream;

import static mdm.xstream.MDMXStreamConstants.*;

public class MDMOutStreamFactory {
    private static XStream x;

    public static synchronized XStream getXStream() {
        if (x == null) {
            x = new XStream(new MDMStaxDriver());

            x.alias(ALIAS_OUT_DOC, OutDoc.class);
            x.useAttributeFor(OutDoc.class, FIELD_ID);
            x.addImplicitArray(OutDoc.class, FIELD_SEGS);

            x.alias(ALIAS_OUT_SEG, OutSeg.class);
            x.useAttributeFor(OutSeg.class, FIELD_ID);
            x.aliasField(ALIAS_SEG_INFO, OutSeg.class, FIELD_SEGINFO);
            x.omitField(OutSeg.class, FIELD_SEGINFO);
            x.aliasField(ALIAS_WORD_INFO, OutSeg.class, FIELD_WORDS);

            x.alias(ALIAS_WORD, Word.class);
            x.useAttributeFor(Word.class, FIELD_ID);
            x.useAttributeFor(Word.class, FIELD_WORD);

            x.alias(ALIAS_ANALYSIS, Analysis.class);
            x.aliasField(ALIAS_MORPH_FS, Analysis.class, FIELD_MORPH_FS);
            // x.useAttributeFor(MorphFS.class, FIELD_LEMMA);
            x.registerConverter(new MorphFSConverter());
        }
        return x;
    }

};
