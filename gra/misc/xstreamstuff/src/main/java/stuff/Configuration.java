package stuff;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

@XStreamAlias("configuration")
public class Configuration {
    public String scheme;

    public Types types;

    @XStreamImplicit(itemFieldName="module")
    public List<Module> modules;
}
