package mdm.xstream.in;

import mdm.in.InSeg;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class InSegConverter implements Converter {
    public boolean canConvert(Class clazz) {
            return InSeg.class == clazz;
    }

    public void marshal(Object value, HierarchicalStreamWriter writer, MarshallingContext context) {
        InSeg seg = (InSeg) value;
        writer.addAttribute("id", seg.getId());
        writer.setValue(seg.getValue());
    }

    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
	throw new UnsupportedOperationException("deserialization is not supported by design here");
    }
};

