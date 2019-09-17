package cl;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import org.ehcache.core.spi.store.heap.LimitExceededException;
import org.ehcache.core.spi.store.heap.SizeOfEngine;
import org.ehcache.core.spi.store.Store;
import org.ehcache.impl.internal.sizeof.DefaultSizeOfEngine;
import org.ehcache.impl.internal.store.heap.holders.OnHeapValueHolder;

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
import java.time.Duration;
import java.util.concurrent.Callable;

import static net.bytebuddy.matcher.ElementMatchers.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App implements CommandLineRunner {

    private static class TestOnHeapValueHolder extends OnHeapValueHolder<String> {
        long now;
        Duration expiration;

        protected TestOnHeapValueHolder(long expirationTime) {
            super(1, 0, expirationTime, true);
        }

        @Override
        public String get() {
            return "test";
        }

        @Override
        public void accessed(long now, Duration expiration) {
            this.now = now;
            this.expiration = expiration;
            super.accessed(now, expiration);
        }
    }

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

    public <K, V> long mysizeof(K key, Store.ValueHolder<V> holder) throws LimitExceededException {
        return 7;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("app start");

        Method bar = App.class.getDeclaredMethod("bar", Integer.class, int.class);
        //Method baz = App.class.getDeclaredMethod("baz");

        ByteBuddyAgent.install();

        /*
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

        Simple simple = new Simple();
        String ret = simple.tell(1, 2);
        log.info("app finish: {}", ret);
        */

        Method mysizeof = App.class.getDeclaredMethod("mysizeof", Object.class, Store.ValueHolder.class);

        new ByteBuddy()
            .redefine(DefaultSizeOfEngine.class)
            .method(named("sizeof"))
            .intercept(
                MethodCall.invoke(mysizeof).withAllArguments()
            )
            .make()
            .load(
                DefaultSizeOfEngine.class.getClassLoader(), 
                ClassReloadingStrategy.fromInstalledAgent()
            );

        SizeOfEngine soe = new DefaultSizeOfEngine(10000, 10000);
        OnHeapValueHolder<String> vh = new TestOnHeapValueHolder(0);

        // default - 224
        long s = soe.sizeof(Integer.valueOf(1), vh);
        log.info("s: {}", s);
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
