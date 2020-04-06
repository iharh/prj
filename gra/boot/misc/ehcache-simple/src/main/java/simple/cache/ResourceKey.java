package simple.cache;

import lombok.Value;

@Value
public class ResourceKey {
    String instanceId;
    long accountId;
}
