package cl;

import cl.cache.ResourceKey;
import cl.cache.ResourceValue;
import cl.cache.service.ResourceCacheService;

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
        doEhc();
    }

    public static void main(String[] args) {
        SpringApplication.run(EhcacheSizeApp.class, args);
    }
}
