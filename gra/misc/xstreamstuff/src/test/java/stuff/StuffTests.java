package stuff;

import org.junit.jupiter.api.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;
// import com.thoughtworks.xstream.io.binary.BinaryStreamDriver;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import java.io.File;
import java.io.FileInputStream;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StuffTests {
    // https://x-stream.github.io/javadoc/index.html
    // https://x-stream.github.io/annotations-tutorial.html

    static class MyType {
        @XStreamAsAttribute public String name;
        @XStreamAsAttribute public int priority;
        @XStreamConverter(value = BooleanConverter.class, booleans = {false,true}, strings = {"on", "off"})
        @XStreamAsAttribute public boolean status;
        @XStreamAsAttribute public String dependsOn; // TODO: alias this
    }

    @XStreamAlias("configuration")
    static class Configuration {
        public String scheme;

        // @XStreamAlias("type")
        // private int messageType;

        @XStreamImplicit(itemFieldName="type")
        public List<MyType> mytypes;
    }

    @Test
    void testXpath() throws Exception {
        // String xmlFileName = "/home/iharh/Downloads/tutorials.xml";
        String xmlFileName = "/home/iharh/Downloads/config.xml";
        // String xmlFileName = "/data/wrk/clb/fx/lang-packs/english/resources/config/config.xml";


        XStream xstream = new XStream(new StaxDriver());
        XStream.setupDefaultSecurity(xstream); // to be removed after 1.5
        xstream.allowTypesByWildcard(new String[] { "stuff.**" });
        assertThat(xstream).isNotNull();
        xstream.processAnnotations( new Class [] { Configuration.class });

        File xmlFile = new File(xmlFileName);

        Configuration cfg = (Configuration)xstream.fromXML(xmlFile);
        assertThat(cfg).isNotNull();
        assertThat(cfg.mytypes).isNotNull();
        assertThat(cfg.mytypes.size()).isEqualTo(2);

        MyType t = cfg.mytypes.get(0);
        assertThat(t).isNotNull();
        assertThat(t.name).isEqualTo("Informal");
        assertThat(t.priority).isEqualTo(15);
        assertThat(t.status).isEqualTo(true);
    }
}
