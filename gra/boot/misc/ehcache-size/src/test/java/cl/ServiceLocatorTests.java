package cl;

import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.spi.ServiceLocator;
import org.ehcache.core.spi.store.heap.SizeOfEngineProvider;
import org.ehcache.impl.config.store.heap.DefaultSizeOfEngineProviderConfiguration;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ServiceLocatorTests {

    @Test
    void testServiceLocator() throws Exception {
        ServiceLocator locator = ServiceLocator.dependencySet().with(
                new DefaultSizeOfEngineProviderConfiguration(1, MemoryUnit.B ,1)
        ).build();
        SizeOfEngineProvider p = locator.getService(SizeOfEngineProvider.class);
        assertThat(p).isNotNull();
        System.out.println("SizeOfEngineProvider impl: " + p.getClass().getName());
    }
}
