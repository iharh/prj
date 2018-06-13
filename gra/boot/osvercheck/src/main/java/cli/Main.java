package cli;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.apache.commons.lang3.SystemUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        boolean isWinServer2008 = SystemUtils.IS_OS_WINDOWS_2008;
        log.warn("isWinServer2008: {}", (isWinServer2008 ? "true" : "false"));
    }

    public static void main(String [] args) {
        SpringApplication app = new SpringApplication(Main.class);
        app.setBannerMode(Banner.Mode.OFF); // OFF LOG
        SpringApplication.run(Main.class, args);
    }
}
