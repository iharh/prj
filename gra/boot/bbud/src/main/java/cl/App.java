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
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.MethodCall;
import net.bytebuddy.implementation.bytecode.assign.Assigner;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.concurrent.Callable;

import static net.bytebuddy.matcher.ElementMatchers.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App implements CommandLineRunner {

    private static class TestOnHeapValueHolder extends OnHeapValueHolder<ResourceValue> {
        long now;
        Duration expiration;

        protected TestOnHeapValueHolder(long expirationTime) {
            super(1, 0, expirationTime, true);
        }

        @Override
        public ResourceValue get() {
            return null;
        }

        @Override
        public void accessed(long now, Duration expiration) {
            this.now = now;
            this.expiration = expiration;
            super.accessed(now, expiration);
        }
    }

    private void doInst() throws Exception {
        ByteBuddyAgent.install(
            new ByteBuddyAgent.AttachmentProvider.Compound(
                new EhcAttachmentProvider(),
                ByteBuddyAgent.AttachmentProvider.DEFAULT
            )
        );

        new ByteBuddy()
            .redefine(DefaultSizeOfEngine.class)
            .visit(Advice.to(TimerAdvice.class).on(named("sizeof")))
            .make()
            .load(
                DefaultSizeOfEngine.class.getClassLoader(), 
                ClassReloadingStrategy.fromInstalledAgent()
            );
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("app start");

        doInst();

        SizeOfEngine soe = new DefaultSizeOfEngine(10000, 10000);

        // default - 224
        long s = soe.sizeof(Integer.valueOf(1), new TestOnHeapValueHolder(0));
        log.info("s: {}", s);
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
