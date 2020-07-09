package stuff;

import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.extended.ToAttributedValueConverter;

@XStreamConverter(value=ToAttributedValueConverter.class, strings={"content"})
public class Resource {
    public String content;
}
