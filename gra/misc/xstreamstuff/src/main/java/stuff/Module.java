package stuff;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

public class Module {
    @XStreamAsAttribute public String path;

    public List<Param> parameters;

    @XStreamImplicit(itemFieldName="resource")
    public List<Resource> resources;
}
