package cl.cache;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.stereotype.Service;

// import javax.annotation.PreDestroy;

import lombok.extern.slf4j.Slf4j;

@Service
@CacheConfig(cacheNames = {"myCache"})
@Slf4j
public class ResourceCacheServiceImpl implements ResourceCacheService {
    // @PreDestroy public void preDestroy() throws Exception { }

    @Override
    @Cacheable(unless = "#result == null")
    public ResourceValue getResourceValue(ResourceKey key) {
        log.info("getByKey for key: {} \tCache MISS!", key);

        return new ResourceValue(false, 0, 1);
    }
}
