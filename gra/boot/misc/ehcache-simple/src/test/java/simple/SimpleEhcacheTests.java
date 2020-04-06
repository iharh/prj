package simple;

import simple.cache.ResourceKey;
import simple.cache.ResourceValue;

import org.junit.jupiter.api.Test;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.ResourceUnit;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.spi.store.Store;
import org.ehcache.core.spi.store.heap.LimitExceededException;
import org.ehcache.core.spi.store.heap.SizeOfEngine;
import org.ehcache.core.spi.store.heap.SizeOfEngineProvider;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;
import org.ehcache.spi.service.ServiceProvider;

//import javax.cache.CacheManager;
//import javax.cache.CacheManagerBuilder;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import static org.assertj.core.api.Assertions.assertThat;
import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;

public class SimpleEhcacheTests {
    private static final int NUM_ENTRIES = 3;
    private static final String INSTANCE_ID = "inst";

    static class ResourceSizeofEngine implements SizeOfEngine {
        @Override
        public <K, V> long sizeof(K key, Store.ValueHolder<V> valueHolder) throws LimitExceededException {
            long nativeSize = 0;
            try {
                final Method methodGet = valueHolder.getClass().getDeclaredMethod("get");
                final Class<?> returnClass = methodGet.getReturnType();
                if (returnClass.isAssignableFrom(ResourceValue.class)) {
                    ResourceValue value = (ResourceValue) methodGet.invoke(valueHolder);
                    nativeSize = value.getNativeSize();
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            }
            
            System.out.println("nativeSize: " + nativeSize);
            return nativeSize;
        }
    }

    @Test
    void testEhcacheSimple() throws Exception {
        try (CacheManager manager = CacheManagerBuilder.newCacheManagerBuilder().using(new SizeOfEngineProvider() {

            @Override
            public void start(ServiceProvider<Service> serviceProvider) {
                System.out.println("Using Custom SizeOfEngineProvider");
            }

            @Override
            public void stop() {
            }

            @Override
            public SizeOfEngine createSizeOfEngine(ResourceUnit resourceUnit, ServiceConfiguration<?, ?>... serviceConfigs) {
                return new ResourceSizeofEngine();
            }
        }).build(true)) {
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
