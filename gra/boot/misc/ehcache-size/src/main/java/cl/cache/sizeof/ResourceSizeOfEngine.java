package cl.cache.sizeof;

import cl.cache.ResourceValue;

import org.ehcache.core.spi.store.Store;
import org.ehcache.core.spi.store.heap.LimitExceededException;
import org.ehcache.core.spi.store.heap.SizeOfEngine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResourceSizeOfEngine implements SizeOfEngine {

    private SizeOfEngine delegateSizeOfEngine;

    public ResourceSizeOfEngine(SizeOfEngine delegateSizeOfEngine) {
        this.delegateSizeOfEngine = delegateSizeOfEngine;
    }

    @Override
    public <K, V> long sizeof(K key, Store.ValueHolder<V> valueHolder) throws LimitExceededException {
        long delegateSize = 0;
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

        if (delegateSizeOfEngine != null) {
            delegateSize = delegateSizeOfEngine.sizeof(key, valueHolder);
        }
        
        log.debug("delegateSize: {}, nativeSize: {}", delegateSize, nativeSize);
        return delegateSize + nativeSize;
    }
}
