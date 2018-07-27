package cl;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomRemovalListener implements RemovalListener<ResourceKey, ResourceValue>{
    @Override
    public void onRemoval(ResourceKey key, ResourceValue value, RemovalCause cause) {
        log.info("removal listerner called with key [{}], cause [{}], evicted [{}]", key, cause.toString(), cause.wasEvicted());
    }
}
