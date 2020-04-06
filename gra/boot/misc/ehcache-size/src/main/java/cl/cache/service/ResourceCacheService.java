package cl.cache.service;

import cl.cache.ResourceKey;
import cl.cache.ResourceValue;

public interface ResourceCacheService {
    ResourceValue getResourceValue(ResourceKey key);
}
