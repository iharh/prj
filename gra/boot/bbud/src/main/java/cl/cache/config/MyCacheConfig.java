package cl.cache.config;

import cl.cache.MyCacheListener;
import cl.cache.ResourceKey;
import cl.cache.ResourceValue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.jcache.JCacheCacheManager;

import org.ehcache.config.CacheConfiguration;
import org.ehcache.config.ResourcePools;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.config.DefaultConfiguration;
import org.ehcache.jsr107.EhcacheCachingProvider;

import javax.cache.CacheManager;
import javax.cache.Caching;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class MyCacheConfig {
    @Value("${my.cache.heap.bytes}")
    private long myCacheHeapBytes;

    @Bean
    public MyCacheListener myCacheListener() {
        return new MyCacheListener();
    }

    @Bean
    public JCacheCacheManager jCacheCacheManager() {
        return new JCacheCacheManager(cacheManager());
    }

    @Bean(destroyMethod = "close")
    public CacheManager cacheManager() {
        log.info("in cacheManager creation myCacheHeapBytes: {}", myCacheHeapBytes);

        ResourcePools resourcePools = ResourcePoolsBuilder.newResourcePoolsBuilder()
            .heap(100, MemoryUnit.MB)
            .build();

        CacheConfiguration<ResourceKey,ResourceValue> cacheConfiguration = CacheConfigurationBuilder.newCacheConfigurationBuilder(
            ResourceKey.class,
            ResourceValue.class,
            resourcePools).
            build();

        Map<String, CacheConfiguration<?, ?>> caches = new HashMap<>();
        caches.put("myCache", cacheConfiguration);

        EhcacheCachingProvider provider = (EhcacheCachingProvider) Caching.getCachingProvider("org.ehcache.jsr107.EhcacheCachingProvider");
        org.ehcache.config.Configuration configuration = new DefaultConfiguration(caches, provider.getDefaultClassLoader());

        return provider.getCacheManager(provider.getDefaultURI(), (org.ehcache.config.Configuration) configuration);
    }
}
