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

import com.csvreader.CsvReader;

import java.io.File;
import java.io.FileInputStream;

import java.nio.charset.StandardCharsets;


@ComponentScan({"cl"})
@EnableCaching
@Slf4j
public class Main implements CommandLineRunner {
    private static final ResourceKey KEY1 = new ResourceKey(0, "lexType1");

    @Autowired private CacheManager cacheManager;
    @Autowired private ResourceService resourceService;

    @Override
    public void run(String... args) throws Exception {
        val reader = new CsvReader(
            new FileInputStream(
                new File("/data/wrk/clb/svnmain/lang-packs/bengali/installer/sql/p_sentiment_modifier.csv")
            ),
            StandardCharsets.UTF_8);

        while (reader.readRecord()) {
            String[] values = reader.getValues();

            int len = values.length;
            if (len != 3) {
                log.info("values length: {}", len);
            }
            for (String v: values) {
                if (v == null) {
                    log.info("value is null !!!");
                }
            }
        }

        /*
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
        */
    }
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
