package cl;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cache.annotation.CacheEvict;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@CacheConfig(cacheNames = {"gazet"})
@Slf4j
public class ResourceService {

    @Cacheable(unless = "#result == null")
    public ResourceValue getByKey(ResourceKey key) {
        log.info("getByKey for key: {} \tCache MISS!", key);
        return new ResourceValue("value-for-" + key.getAccountId() + "-" + key.getLexiconType());
    }

    @Caching(evict = {
        // @CacheEvict(value = "other", allEntries = true),
        @CacheEvict(value = "gazet", allEntries = true)
    })
    public void clearAll() {
        log.info("cleared all caches");
    }
}
