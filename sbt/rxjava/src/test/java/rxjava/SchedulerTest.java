package rxjava;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;

import rx.Observable;
import rx.schedulers.Schedulers;
import rx.functions.Action1;


import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulerTest {
    private static final Logger log = LoggerFactory.getLogger(SchedulerTest.class);

    @Test
    public void testScheduler() throws Exception {
        final CountDownLatch finish = new CountDownLatch(1); 

        Observable
            .range(0, 100)
            .subscribeOn(Schedulers.computation())
            .observeOn(Schedulers.computation())
            .forEach(new Action1<Integer>() {
                @Override
                public void call(Integer l) {
                    log.info("call: {}", l);
                }
            });
/*
            .subscribe(new Subscriber<Long>() {
                @Override
                public void onCompleted() {
                    log.info("onCompleted");
                }

                @Override
                public void onError(Throwable e) {
                    log.error("onError");
                }

                @Override
                public void onNext(Long arg) {
                    log.info("onNext");
                }
            })
            .toBlocking();
*/
        log.info("sleep...");
        Thread.currentThread().sleep(1000);
        log.info("done");

        assertTrue(true);
    }
}
