package cl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.CacheManager;
import org.springframework.cache.Cache;

import java.util.concurrent.TimeUnit;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@ComponentScan({"cl"})
@EnableCaching
@Slf4j
public class Main implements CommandLineRunner {
    private static final String KEY1 = "Key1";

    @Autowired private CacheManager cacheManager;
    @Autowired private ResourceService resourceService;

    @Override
    public void run(String... args) throws Exception {
        log.info("cacheManager null: {}", cacheManager == null);
        log.info("resourceService null: {}", resourceService == null);

        resourceService.getByKey(KEY1);
        log.info("done 1");
        resourceService.getByKey(KEY1);
        log.info("done 2");
        resourceService.getByKey(KEY1);
        log.info("done 3");


        log.info("sleep 3 sec");
        TimeUnit.SECONDS.sleep(3);

        resourceService.getByKey(KEY1);
        log.info("done 4");
        resourceService.getByKey(KEY1);
        log.info("done 5");
        resourceService.getByKey(KEY1);
        log.info("done 6");

        TimeUnit.SECONDS.sleep(7);
        log.info("done sleep");
    }
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
