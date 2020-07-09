package stuff;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

@XStreamAlias("param")
@XStreamConverter(value=ToAttributedValueConverter.class, strings={"content"})
public class Param {
    @XStreamAsAttribute public String name;
    public String content;
}
