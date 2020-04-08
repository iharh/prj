package cl.cache.sizeof;

import org.ehcache.core.spi.service.ServiceFactory;
import org.ehcache.core.spi.store.heap.SizeOfEngineProvider;
import org.ehcache.impl.config.store.heap.DefaultSizeOfEngineConfiguration;
import org.ehcache.impl.config.store.heap.DefaultSizeOfEngineProviderConfiguration;
import org.ehcache.spi.service.ServiceCreationConfiguration;

// clb! 
// import org.osgi.service.component.annotations.Component;

// cbl! @Component
public class ResourceSizeOfEngineProviderFactory implements ServiceFactory<SizeOfEngineProvider> {

    @Override
    public SizeOfEngineProvider create(ServiceCreationConfiguration<SizeOfEngineProvider, ?> configuration) {
        long maxTraversals = DefaultSizeOfEngineConfiguration.DEFAULT_OBJECT_GRAPH_SIZE;
        long maxSize = DefaultSizeOfEngineConfiguration.DEFAULT_MAX_OBJECT_SIZE;
        if (configuration != null) {
            DefaultSizeOfEngineProviderConfiguration sizeOfEngineConfiguration = (DefaultSizeOfEngineProviderConfiguration)configuration;
            maxTraversals = sizeOfEngineConfiguration.getMaxObjectGraphSize();
            maxSize = sizeOfEngineConfiguration.getUnit().toBytes(sizeOfEngineConfiguration.getMaxObjectSize());
        }
        return new ResourceSizeOfEngineProvider(); // clb! TODO: (maxTraversals, maxSize);
    }

    @Override
    public Class<SizeOfEngineProvider> getServiceType() {
        return SizeOfEngineProvider.class;
    }

    @Override
    public int rank() {
        return 2; // in order to have precedence over existing ehcache DefaultSizeOfEngineProviderFactory 
    }
}
