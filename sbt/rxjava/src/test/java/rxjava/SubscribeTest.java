package rxjava;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;

import rx.Observable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubscribeTest {
    private static final Logger log = LoggerFactory.getLogger(SubscribeTest.class);

    @Test
    public void testScheduler() throws Exception {
        Observable<Integer> baseObservable = Observable.range(0, 5);
        baseObservable.subscribe(
            i -> { log.info("onNext: {}", i); }
        );
        assertTrue(true);
    }
}
