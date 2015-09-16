package rxjava;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;


import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchedulerTest {
    private static final Logger log = LoggerFactory.getLogger(SchedulerTest.class);

    static class MySubscriber extends Subscriber<Integer> {
        private CountDownLatch finish;
        private String p;

        public MySubscriber(CountDownLatch finish, String p) {
            this.finish = finish;
            this.p = p;
        }

        @Override
        public void onCompleted() {
            log.info("onCompleted {}", p);
            finish.countDown();
        }

        @Override
        public void onError(Throwable e) {
            log.error("onError");
        }

        @Override
        public void onNext(Integer arg) {
            log.info("onNext {} {}", p, arg);
            try {
                Thread.currentThread().sleep(20);
            } catch (InterruptedException e) {
            }
        }
    };

    @Test
    public void testScheduler() throws Exception {
        final int NUM_THREADS = 3;
        final CountDownLatch finish = new CountDownLatch(NUM_THREADS);

        final MySubscriber s1 = new MySubscriber(finish, "s1");
        final MySubscriber s2 = new MySubscriber(finish, "s2");
        final MySubscriber s3 = new MySubscriber(finish, "s3");

        Observable<Integer> baseObservable = Observable.range(0, 50)
            .subscribeOn(Schedulers.computation());

        for (int i = 0; i < NUM_THREADS; ++i) {
            baseObservable
                .subscribe(new MySubscriber(finish, "s" + i));
        }

        log.info("done chain creating");
        finish.await();
        log.info("done waiting");

        assertTrue(true);
    }
}
