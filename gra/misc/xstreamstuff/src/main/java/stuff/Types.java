package stuff;

import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

public class Types {
    public Untyped untyped;

    @XStreamImplicit(itemFieldName="type")
    public List<Type> elements;
}
