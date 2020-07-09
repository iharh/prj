package stuff;

import org.junit.jupiter.api.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.XStreamException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import java.io.File;
import java.io.FileInputStream;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class StuffTests {
    // https://x-stream.github.io/javadoc/index.html
    // https://x-stream.github.io/annotations-tutorial.html

    static class Untyped {
        @XStreamAsAttribute public int priority;
    }

    static class Type {
        @XStreamAsAttribute public String name;
        @XStreamAsAttribute public int priority;
        @XStreamConverter(value = BooleanConverter.class, booleans = {false,true}, strings = {"on", "off"})
        @XStreamAsAttribute public boolean status;
        @XStreamAsAttribute public String dependsOn; // TODO: alias this
    }

    static class Types {
        public Untyped untyped;

        @XStreamImplicit(itemFieldName="type")
        public List<Type> elements;
    }

    static class Parameters {
    }

    static class CfgModule {
        @XStreamAsAttribute public String path;
        public Parameters parameters;
    }

    @XStreamAlias("configuration")
    static class Configuration {
        public String scheme;

        public Types types;

        @XStreamImplicit(itemFieldName="module")
        public List<CfgModule> modules;
    }

    @Test
    void testXpath() throws Exception {
        String xmlFileName = "/home/iharh/Downloads/config.xml";
        // String xmlFileName = "/data/wrk/clb/fx/lang-packs/english/resources/config/config.xml";

        XStream xstream = new XStream(new StaxDriver());
        XStream.setupDefaultSecurity(xstream); // to be removed after 1.5
        xstream.allowTypesByWildcard(new String[] { "stuff.**" });
        xstream.processAnnotations(new Class [] { Configuration.class });
        // can't find appropriate annotation for this
        xstream.aliasAttribute(Type.class, "dependsOn", "depends-on");

        File xmlFile = new File(xmlFileName);

        Configuration cfg = (Configuration)xstream.fromXML(xmlFile);
        assertThat(cfg).isNotNull();
        assertThat(cfg.scheme).isEqualTo("en/scheme/english.scheme");

        Types types = cfg.types;
        assertThat(types).isNotNull();
        assertThat(types.untyped).isNotNull();
        assertThat(types.untyped.priority).isEqualTo(10);
        assertThat(types.elements).isNotNull();
        assertThat(types.elements.size()).isEqualTo(38);

        Type t = types.elements.get(0);
        assertThat(t).isNotNull();
        assertThat(t.name).isEqualTo("Informal");
        assertThat(t.priority).isEqualTo(15);
        assertThat(t.status).isEqualTo(true);

        t = types.elements.get(13);
        assertThat(t).isNotNull();
        assertThat(t.dependsOn).isEqualTo("Numeric");

        assertThat(cfg.modules).isNotNull();
        assertThat(cfg.modules.size()).isEqualTo(1);
        
        CfgModule m = cfg.modules.get(0);
        assertThat(m).isNotNull();
        assertThat(m.path).isEqualTo("text-parser.jar");
    }
}
