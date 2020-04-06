package cl.cache.service;

import cl.cache.ResourceKey;
import cl.cache.ResourceValue;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@CacheConfig(cacheNames = {"myCache"})
@Slf4j
public class ResourceCacheServiceImpl implements ResourceCacheService {

    @Override
    @Cacheable(unless = "#result == null")
    public ResourceValue getResourceValue(ResourceKey key) {
        log.info("getByKey for key: {} \tCache MISS!", key);

        return new ResourceValue(false, 0, 1);
    }
}
