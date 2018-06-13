package cli;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.apache.commons.lang3.SystemUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Main implements CommandLineRunner {

    private boolean isWinServer2008() {
        final String osName = SystemUtils.OS_NAME;
        return osName != null && osName.startsWith("Windows Server 2008");
    }

    @Override
    public void run(String... args) throws Exception {
        log.warn("os.name: {}, os.version: {}", SystemUtils.OS_NAME, SystemUtils.OS_VERSION);
        log.warn("isWinServer2008: {}", (isWinServer2008() ? "true" : "false"));
    }

    public static void main(String [] args) {
        SpringApplication app = new SpringApplication(Main.class);
        app.setBannerMode(Banner.Mode.OFF); // OFF LOG
        SpringApplication.run(Main.class, args);
    }
}
