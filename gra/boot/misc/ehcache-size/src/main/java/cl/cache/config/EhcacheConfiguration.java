package cl.cache.config;

import cl.cache.ResourceKey;
import cl.cache.ResourceValue;

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

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class EhcacheConfiguration {
    @Bean
    public JCacheCacheManager jCacheCacheManager() {
        return new JCacheCacheManager(cacheManager());
    }

    @Bean(destroyMethod = "close")
    public CacheManager cacheManager() {
        log.info("enter cacheManager");

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

        EhcacheCachingProvider provider = (EhcacheCachingProvider) Caching.getCachingProvider(EhcacheCachingProvider.class.getName());
        org.ehcache.config.Configuration configuration = new DefaultConfiguration(caches, provider.getDefaultClassLoader());

        // ??? what if define create "org.ehcache.CacheManager" as a wrapper of "javax.cache.cacheManager" ???
        return provider.getCacheManager(provider.getDefaultURI(), configuration);
    }
}
