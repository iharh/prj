package cl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.cache.CacheManager;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main implements CommandLineRunner {

    private final CacheManager cacheManager;
    // private final ResourceService resourceService;

    // @Autowired
    public Main(CacheManager cacheManager/*, ResourceService resourceService*/) {
        this.cacheManager = cacheManager;
        // this.resourceService = resourceService;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("cacheManager null: {}", (cacheManager == null ? "true" : "false"));
    }
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
