package stuff;

import org.junit.jupiter.api.Test;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import java.io.File;
import java.io.FileWriter;

import static org.assertj.core.api.Assertions.assertThat;

public class StuffTests {

    @Test
    void testXpath() throws Exception {
        String xmlInputFileName = "/data/wrk/clb/fx/lang-packs/english/resources/config/config.xml";
        String xmlOutputFileName = "/home/iharh/Downloads/out.xml";

        XStream xstream = new XStream(new StaxDriver());
        XStream.setupDefaultSecurity(xstream); // to be removed after 1.5
        xstream.allowTypesByWildcard(new String[] { "stuff.**" });
        xstream.processAnnotations(new Class [] { Configuration.class });

        Configuration cfg = (Configuration)xstream.fromXML(new File(xmlInputFileName));
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
        assertThat(cfg.modules.size()).isEqualTo(25);
        
        Module m = cfg.modules.get(0);
        assertThat(m).isNotNull();
        assertThat(m.path).isEqualTo("text-parser.jar");
        assertThat(m.parameters).isNotNull();
        assertThat(m.parameters.size()).isEqualTo(1);
        Param p = m.parameters.get(0);
        assertThat(p).isNotNull();
        assertThat(p.name).isEqualTo("plain-text.block");
        assertThat(p.content).isNotNull();
        assertThat(p.content).isEqualTo("\\r?\\n(?:\\r?\\n|(?=(?:[ \\t]+(?:\\-|[ ]{2,}))))");

        assertThat(m.resources).isNotNull();
        assertThat(m.resources.size()).isEqualTo(1);
        Resource r = m.resources.get(0);
        assertThat(r).isNotNull();
        assertThat(r.content).isNotNull();
        assertThat(r.content).isEqualTo("en/break-rule/sentence-recombination.brl");

        xstream.marshal(cfg, new PrettyPrintWriter(new FileWriter(xmlOutputFileName)));
    }
}
