package cl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.cache.CacheManager;

import lombok.extern.slf4j.Slf4j;

@ComponentScan({"cl"})
@Slf4j
public class Main implements CommandLineRunner {

    @Autowired private CacheManager cacheManager;
    // private final ResourceService resourceService;

    @Override
    public void run(String... args) throws Exception {
        log.info("cacheManager null: {}", (cacheManager == null ? "true" : "false"));
    }
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
