package mdm.xstream.out;

import mdm.out.MorphFS;
import mdm.out.MorphFSAttr;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import com.ibm.icu.text.Normalizer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.List;
import java.util.ArrayList;

import static mdm.xstream.MDMXStreamConstants.*;

public class MorphFSConverter implements Converter {

    private Pattern lemmaPat;

    public MorphFSConverter() {
        lemmaPat = Pattern.compile("(\\-َ|\\-ُ|\\-َِ|\\-َِ|\\-ِ|\\-ُِ)?_\\d+$");
    }

    public boolean canConvert(Class clazz) {
            return MorphFS.class == clazz;
    }

    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        throw new UnsupportedOperationException("serialization is not supported by design here");
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        MorphFS morphFS = new MorphFS();

        int attrCount = reader.getAttributeCount();
        List<MorphFSAttr> attrs = new ArrayList<MorphFSAttr>(attrCount - 1);

        for (int i = 0; i < attrCount; ++i) {
            String attrName = reader.getAttributeName(i);
            String attrValue = reader.getAttribute(i);

            if (FIELD_LEMMA.equals(attrName)) {
                morphFS.setLemma(Normalizer.compose(lemmaPat.matcher(attrValue).replaceAll(""), false));
            } else {
                attrs.add(new MorphFSAttr(attrName, attrValue));
            }
        }

        morphFS.setAttrs(attrs.toArray(new MorphFSAttr [0]));

        return morphFS;
    }
};

