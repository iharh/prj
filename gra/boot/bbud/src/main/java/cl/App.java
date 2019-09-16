package cl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

import static net.bytebuddy.matcher.ElementMatchers.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App implements CommandLineRunner {

    public static class TimingInterceptor {
        @RuntimeType
        public static Object intercept(@Origin Method method, @SuperCall Callable<?> callable) throws Exception {
            long start = System.currentTimeMillis();
            try {
                return callable.call();
            } finally {
                System.out.println(method + " took " + (System.currentTimeMillis() - start));
            }
        }
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("app start");

        ByteBuddyAgent.install();
        new ByteBuddy()
            .redefine(Simple.class)
            .method(named("tell"))
            .intercept(MethodDelegation.to(new TimingInterceptor()))
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
