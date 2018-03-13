package sample;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import com.ibm.icu.text.Normalizer;
//import com.ibm.icu.text.Normalizer2;

public class Main implements CommandLineRunner {
    private static void compDecomp(String orig) {
        String compF = Normalizer.compose(orig, false);
        String compT = Normalizer.compose(orig, true);
        String decompF = Normalizer.decompose(orig, false);
        String decompT = Normalizer.decompose(orig, true);
        System.out.println(orig + " " + compF + " " + compT + " " + decompF + " " + decompT);
    }

    @Override
    public void run(String... args) throws Exception {
        compDecomp("ộ");
    }

    public static void main(String [] args) {
        SpringApplication app = new SpringApplication(Main.class);
        app.setBannerMode(Banner.Mode.OFF); // OFF LOG
        SpringApplication.run(Main.class, args);
    }
}
