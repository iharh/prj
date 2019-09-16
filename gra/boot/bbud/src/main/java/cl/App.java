package cl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;

import static net.bytebuddy.matcher.ElementMatchers.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        log.info("app start");

        ByteBuddyAgent.install();
        new ByteBuddy()
            .redefine(Simple.class)
            .method(named("tell"))
            .intercept(FixedValue.value("Hello Foo Redefined"))
            .make()
            .load(
                Simple.class.getClassLoader(), 
                ClassReloadingStrategy.fromInstalledAgent()
            );

        Simple s = new Simple();
        String ret = s.tell();
        log.info("app finish: {}", ret);
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
