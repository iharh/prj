package cl.cache.config;

import org.ehcache.config.ResourceUnit;
import org.ehcache.core.spi.store.heap.SizeOfEngine;
import org.ehcache.core.spi.store.heap.SizeOfEngineProvider;
import org.ehcache.spi.service.Service;
import org.ehcache.spi.service.ServiceConfiguration;
import org.ehcache.spi.service.ServiceProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResourceSizeOfEngineProvider implements SizeOfEngineProvider {
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
        return new ResourceSizeOfEngine();
    }
}
