package cl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodCall;
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
        /*@RuntimeType
        public static Object intercept(@Origin Method method, @SuperCall Callable<?> callable) {
            long start = System.currentTimeMillis();
            try {
                return callable.call();
            } catch (Exception e) {
                System.out.println("caught: " + e.getMessage());
            } finally {
                System.out.println(method + " took " + (System.currentTimeMillis() - start));
            }
            return null;
        }*/

        @RuntimeType
        public static Object intercept(@Origin Method method) {
            return "ddd bbb eee";
        }
    }

    public static String bar(Integer n, int k) {
        log.info("bar called");
        return "do bar n:" + n + ", k=" + k;
    }
    public static String baz(Integer n, int k) {
        return "do baz";
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("app start");

        Method bar = App.class.getDeclaredMethod("bar", Integer.class, int.class);
        //Method baz = App.class.getDeclaredMethod("baz");

        ByteBuddyAgent.install();
        new ByteBuddy()
            .redefine(Simple.class)
            .method(named("tell"))
            //.intercept(MethodDelegation.to(new TimingInterceptor()))
            //.intercept(FixedValue.value("Hello Foo Redefined"))
            .intercept(
                MethodCall.invoke(bar)                 // call bar()...
                //.andThen(MethodCall.invoke(baz))  // ... .length()?
                .withAllArguments()
            )
            .make()
            .load(
                Simple.class.getClassLoader(), 
                ClassReloadingStrategy.fromInstalledAgent()
            );

        Simple s = new Simple();
        String ret = s.tell(1, 2);
        log.info("app finish: {}", ret);
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
