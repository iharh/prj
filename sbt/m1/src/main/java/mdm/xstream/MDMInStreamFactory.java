package mdm.xstream;

import mdm.in.InDoc;
import mdm.in.InSeg;

import mdm.xstream.in.InSegConverter;

import com.thoughtworks.xstream.XStream;

import static mdm.xstream.MDMXStreamConstants.*;

public class MDMInStreamFactory {
    private static XStream x;

    public static synchronized XStream getXStream() {
        if (x == null) {
            x = new XStream(new MDMStaxDriver());

            x.alias(ALIAS_IN_DOC, InDoc.class);
            x.useAttributeFor(InDoc.class, FIELD_ID);
            x.addImplicitArray(InDoc.class, FIELD_SEGS);

            x.alias(ALIAS_IN_SEG, InSeg.class);
            x.useAttributeFor(InSeg.class, FIELD_ID);
            x.registerConverter(new InSegConverter());
        }
        return x;
    }

};
