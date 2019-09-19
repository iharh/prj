package cl;

import cl.bytebuddy.TimerAdvice;
import cl.cache.ResourceCacheService;
import cl.cache.ResourceKey;
import cl.cache.ResourceValue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.CacheManagerBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.core.spi.store.heap.LimitExceededException;
import org.ehcache.core.spi.store.heap.SizeOfEngine;
import org.ehcache.core.spi.store.Store;
import org.ehcache.impl.internal.sizeof.DefaultSizeOfEngine;
import org.ehcache.impl.internal.store.heap.holders.OnHeapValueHolder;

import org.ehcache.core.Ehcache;

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

@SpringBootApplication
@EnableCaching
@Slf4j
public class App implements CommandLineRunner {
    private SizeOfEngine soe;

    @Autowired
    private ResourceCacheService resourceCacheService;

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
        log.info("doInst");

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

        soe = new DefaultSizeOfEngine(10000, 10000);
    }

    private void doSingle() throws Exception {
        log.info("doSingle");

        // default - 224
        long s = soe.sizeof(Integer.valueOf(1), new TestOnHeapValueHolder(0));
        log.info("s: {}", s);
    }

    private void doEhc() throws Exception {
        log.info("doEhc");

        CacheManager cacheManager = CacheManagerBuilder.newCacheManagerBuilder()
            .withCache("preConfigured",
                CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                    ResourcePoolsBuilder.newResourcePoolsBuilder().heap(100, MemoryUnit.MB).build())
                .build())
            .build(true);

        Cache<Long, String> preConfigured
            = cacheManager.getCache("preConfigured", Long.class, String.class);

        log.info("cache class: {}", preConfigured.getClass());

        Ehcache ehc = (Ehcache) preConfigured;
        // EhcacheBase stuff

        /*Cache<Long, String> myCache = cacheManager.createCache("myCache",
            CacheConfigurationBuilder.newCacheConfigurationBuilder(Long.class, String.class,
                ResourcePoolsBuilder.heap(100)
            ).build()
        );

        myCache.put(1L, "da one!");
        String value = myCache.get(1L);
        */

        preConfigured.put(1L, "da one!");
        String value = preConfigured.get(1L);

        cacheManager.close();
    }
    
    private void doJcache() throws Exception {
        log.info("doJcache");
        final ResourceKey key = new ResourceKey("cmpId", 0);
        final ResourceValue value = resourceCacheService.getResourceValue(key);
        log.info("value: {}", value);
    }

    @Override
    public void run(String... args) throws Exception {
        doInst();
        doSingle();
        // doEhc();
        doJcache();
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
