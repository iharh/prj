package cl;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
public class CacheJavaConfig {
    @Getter
    @Value("${caffeine.spec}")
    private String caffeineSpecStr; // initialCapacity=100,maximumSize=1000,expireAfterWrite=2s,recordStats

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("gazet", "cte");
        cacheManager.setAllowNullValues(false); //can happen if you get a value from a @Cachable that returns null
        Caffeine<ResourceKey, ResourceValue> caffeine = caffeineCacheBuilder();
        // Caffeine<Object, Object> caffeineObj = (Caffeine<Object, Object>)caffeine;
        cacheManager.setCaffeine(caffeine);
        return cacheManager;
    }

    private Caffeine<ResourceKey, ResourceValue> caffeineCacheBuilder() {
        return Caffeine.from(caffeineSpec())
            .removalListener(new CustomRemovalListener());
    }

    private CaffeineSpec caffeineSpec() {
        return CaffeineSpec.parse(caffeineSpecStr);
    }
}
