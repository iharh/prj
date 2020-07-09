package stuff;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.converters.basic.BooleanConverter;

public class Type {
    @XStreamAsAttribute public String name;
    @XStreamAsAttribute public int priority;
    @XStreamConverter(value = BooleanConverter.class, booleans = {false,true}, strings = {"on", "off"})
    @XStreamAsAttribute public boolean status;
    @XStreamAlias("depends-on") @XStreamAsAttribute public String dependsOn;
}
