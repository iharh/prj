package cl;

import cl.cache.ResourceKey;
import cl.cache.ResourceValue;
import cl.cache.sizeof.ResourceSizeOfEngineProvider;

import org.junit.jupiter.api.Test;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;

public class CacheTests {
    private static final int NUM_ENTRIES = 3;
    private static final String INSTANCE_ID = "inst";

    @Test
    void testEhcacheSimple() throws Exception {
        try (CacheManager manager = CacheManagerBuilder.newCacheManagerBuilder().using(new ResourceSizeOfEngineProvider()).build(true)) {
            Cache<ResourceKey, ResourceValue> cache = manager.createCache("testCache",
                    newCacheConfigurationBuilder(ResourceKey.class, ResourceValue.class,
                        ResourcePoolsBuilder.newResourcePoolsBuilder().heap(NUM_ENTRIES, MemoryUnit.B)));

            for (int i = 0; i <= NUM_ENTRIES; ++i) {
                cache.put(new ResourceKey(INSTANCE_ID, i), new ResourceValue(false, 0, 1));
            }

            assertThat(cache).hasSize(NUM_ENTRIES);
        }
    }
}
