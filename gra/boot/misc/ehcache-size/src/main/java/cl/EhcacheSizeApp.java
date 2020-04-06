package cl;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@EnableCaching
@Slf4j
public class EhcacheSizeApp implements CommandLineRunner {

    private void doEhc() throws Exception {
        log.info("doEhc");
    }
    
    @Override
    public void run(String... args) throws Exception {
        doEhc();
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
