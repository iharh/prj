package mpkg;

import mdm.MDMProcessor;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;

import java.io.OutputStream;
import java.io.FileOutputStream;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


import org.junit.Test;
import org.junit.Ignore;
import org.junit.Before;
import org.junit.After;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class HttpTest {
    private static final int RUNS = 10; // 10000

    private static class FailureRecordingCallback implements FutureCallback<Integer> {
        private Throwable t;

        @Override
        public void onFailure(Throwable t) {
            this.t = t;
        }
        @Override
        public void onSuccess(Integer result) {
            // do something with the result
        }

        public void assertT() throws Exception {
            if (t != null)
                throw new Exception(t.getMessage(), t);
        }
    }

    @Before
    public void beforeTest() throws Exception {
        MDMProcessor.init();
    }

    @After
    public void afterTest() throws Exception {
        MDMProcessor.release();
    }

    @Ignore 
    public void testHttpParallel() throws Exception {
        ListeningExecutorService executor = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(4));
        FailureRecordingCallback callback = new FailureRecordingCallback();

        for (int k=0; k < RUNS; ++k) {
            Callable<Integer> job = new Callable<Integer>() {
                @Override
                public Integer call() throws Exception {
                    MDMTestUtils.checkOutDoc1(MDMProcessor.process(MDMTestUtils.getInDoc1()));
                    return 0;
                }
            };

            ListenableFuture<Integer> completion = executor.submit(job);
            Futures.addCallback(completion, callback);
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }

        callback.assertT();
        assertTrue(true);
    }

    @Ignore 
    public void testHttpSequential() throws Exception {
        for (int k = 0; k < RUNS; ++k) {
            MDMTestUtils.checkOutDoc1(MDMProcessor.process(MDMTestUtils.getInDoc1()));
        };
        assertTrue(true);
    }
};
