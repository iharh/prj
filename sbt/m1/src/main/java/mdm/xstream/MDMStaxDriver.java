package mdm.xstream;

import com.thoughtworks.xstream.io.xml.StaxDriver;
import com.thoughtworks.xstream.io.xml.StaxWriter;
import com.thoughtworks.xstream.io.naming.NoNameCoder;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

public class MDMStaxDriver extends StaxDriver {
    private static final String DEF_NAMESPACE = "urn:edu.columbia.ccls.madamira.configuration:0.1";

    public MDMStaxDriver() {
        super(new NoNameCoder());
        getQnameMap().setDefaultNamespace(DEF_NAMESPACE);
    }

    public StaxWriter createStaxWriter(XMLStreamWriter out) throws XMLStreamException {
        return createStaxWriter(out, false);
    }
};

