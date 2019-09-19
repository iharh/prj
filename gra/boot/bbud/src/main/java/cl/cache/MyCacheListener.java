package cl.cache;

//import javax.cache.event.CacheEntryCreatedListener;
import javax.cache.event.CacheEntryExpiredListener;
//import javax.cache.event.CacheEntryRemovedListener;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryListenerException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyCacheListener implements
    //CacheEntryCreatedListener<ResourceKey, ResourceValue>,
    //CacheEntryRemovedListener<ResourceKey, ResourceValue>,
    CacheEntryExpiredListener<ResourceKey, ResourceValue> {

    public MyCacheListener() {
    }

    @Override
    public void onExpired(Iterable<CacheEntryEvent<? extends ResourceKey, ? extends ResourceValue>> cacheEntryEvents)
        throws CacheEntryListenerException {
        log.info("ehcache onExpired called");
        for (CacheEntryEvent<? extends ResourceKey, ? extends ResourceValue> evt : cacheEntryEvents) {
            ResourceValue val = evt.getOldValue();
            log.info("eventType: {} oldValue: {}, value: {}", evt.getEventType(), val, evt.getValue());
            // if (val != null) { ... }
        }
    }

    /*
    @Override
    public void onCreated(Iterable<CacheEntryEvent<? extends ResourceKey, ? extends ResourceValue>> cacheEntryEvents)
        throws CacheEntryListenerException {
        log.info("ehcache onCreated called");
    }

    @Override
    public void onRemoved(Iterable<CacheEntryEvent<? extends ResourceKey, ? extends ResourceValue>> cacheEntryEvents)
        throws CacheEntryListenerException {
        log.info("ehcache onRemoved called");
    }
    */
}
