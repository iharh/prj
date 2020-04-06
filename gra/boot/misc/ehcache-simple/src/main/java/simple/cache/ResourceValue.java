package simple.cache;

import lombok.Value;

@Value
public class ResourceValue {
    boolean predefined;
    long ptrLexiconResGrp;

    long nativeSize;    
}
