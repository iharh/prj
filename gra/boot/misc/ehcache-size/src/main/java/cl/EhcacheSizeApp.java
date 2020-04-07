package cl;

import cl.cache.ResourceKey;
import cl.cache.ResourceValue;
import cl.cache.service.ResourceCacheService;

import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.spi.ServiceLocator;
import org.ehcache.core.spi.store.heap.SizeOfEngineProvider;
import org.ehcache.impl.config.store.heap.DefaultSizeOfEngineProviderConfiguration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableCaching
@Slf4j
public class EhcacheSizeApp implements CommandLineRunner {

    @Autowired
    private ResourceCacheService resourceCacheService;

    private void doEhc() throws Exception {
        final ResourceKey key = new ResourceKey("cmpId", 0);
        final ResourceValue value = resourceCacheService.getResourceValue(key);
        log.info("got cached value: {}", value);
    }
    
    @Override
    public void run(String... args) throws Exception {
        ServiceLocator.DependencySet dependencySet = ServiceLocator.dependencySet();
        ServiceLocator locator = dependencySet.with(new DefaultSizeOfEngineProviderConfiguration(1, MemoryUnit.B ,1)).build();
        SizeOfEngineProvider p = locator.getService(SizeOfEngineProvider.class); // ! not a mandatory service

        doEhc();
    }

    public static void main(String[] args) {
        SpringApplication.run(EhcacheSizeApp.class, args);
    }
}
