package cl;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CacheTests {

    @Test
    void processMe() throws Exception {
        assertThat(0).isEqualTo(1);
    }
}

        /*
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
                return new SizeOfEngine() {
                    @Override
                    public <K, V> long sizeof(K key, Store.ValueHolder<V> holder) throws LimitExceededException {
                        return 1;
                    }
                };
            }
        }).build(true)) {
            Cache<Number, CharSequence> cache = manager.createCache("testCache", newCacheConfigurationBuilder(Number.class, CharSequence.class,
            ResourcePoolsBuilder.newResourcePoolsBuilder().heap(10, MemoryUnit.B)));

            cache.put(1, "one");
            cache.put(2, "two");
            cache.put(3, "three");
            cache.put(4, "four");

            assertThat(cache, iterableWithSize(4));
        }
        */
