package cl.cache.sizeof;

import org.ehcache.config.ResourceUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.spi.service.ServiceUtils;
import org.ehcache.core.spi.store.heap.SizeOfEngine;
import org.ehcache.core.spi.store.heap.SizeOfEngineProvider;
import org.ehcache.impl.config.store.heap.DefaultSizeOfEngineConfiguration;
import org.ehcache.impl.internal.sizeof.DefaultSizeOfEngine;
import org.ehcache.impl.internal.sizeof.NoopSizeOfEngine;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;
import org.ehcache.spi.service.ServiceProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResourceSizeOfEngineProvider implements SizeOfEngineProvider {

    private final long maxObjectGraphSize;
    private final long maxObjectSize;

    public ResourceSizeOfEngineProvider(long maxObjectGraphSize, long maxObjectSize) {
        this.maxObjectGraphSize = maxObjectGraphSize;
        this.maxObjectSize = maxObjectSize;
    }

    @Override
    public void start(ServiceProvider<Service> serviceProvider) {
        log.debug("start ResourceSizeOfEngineProvider");
    }

    @Override
    public void stop() {
        log.debug("stop ResourceSizeOfEngineProvider");
    }

    @Override
    public SizeOfEngine createSizeOfEngine(ResourceUnit resourceUnit, ServiceConfiguration<?, ?>... serviceConfigs) {
        boolean isByteSized = resourceUnit instanceof MemoryUnit;
        if (!isByteSized) {
            return new NoopSizeOfEngine();
        }
        DefaultSizeOfEngineConfiguration config = ServiceUtils.findSingletonAmongst(DefaultSizeOfEngineConfiguration.class, (Object[]) serviceConfigs);

        SizeOfEngine delegateSizeOfEngine;
        if (config != null) {
            long maxSize = config.getUnit().toBytes(config.getMaxObjectSize());
            delegateSizeOfEngine = new DefaultSizeOfEngine(config.getMaxObjectGraphSize(), maxSize);
        }
        delegateSizeOfEngine = new DefaultSizeOfEngine(maxObjectGraphSize, maxObjectSize);

        return new ResourceSizeOfEngine(delegateSizeOfEngine);
    }
}
