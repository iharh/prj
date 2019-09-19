package cl.cache.config;

import cl.cache.MyCacheListener;
import cl.cache.ResourceKey;
import cl.cache.ResourceValue;

import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.configuration.MutableCacheEntryListenerConfiguration;
import javax.cache.configuration.FactoryBuilder;
import javax.cache.expiry.CreatedExpiryPolicy;
import javax.cache.expiry.Duration;

import java.util.concurrent.TimeUnit;

@Configuration
public class MyCacheConfig {

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return new JCacheManagerCustomizer() {
            @Override
            public void customize(CacheManager cacheManager) {
                FactoryBuilder.SingletonFactory<MyCacheListener> listenerFactory =
                    new FactoryBuilder.SingletonFactory<MyCacheListener>(myCacheListener());

                cacheManager.createCache("myCache", new MutableConfiguration<ResourceKey, ResourceValue>()
                    // TODO: need a memory-based config here !!!
                    .setExpiryPolicyFactory(CreatedExpiryPolicy.factoryOf(new Duration(TimeUnit.MINUTES, 1)))
                    .setStoreByValue(false)
                    .setStatisticsEnabled(true)
                    .addCacheEntryListenerConfiguration(
                        new MutableCacheEntryListenerConfiguration<ResourceKey, ResourceValue>(
                            listenerFactory, null, false, true
                    )));
            }
        };
    }

    @Bean
    public MyCacheListener myCacheListener() {
        return new MyCacheListener();
    }
}
