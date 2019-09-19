package cl.cache;

import lombok.Value;

@Value
public class ResourceKey {
    String cmpId;
    long accountId;
}
