package com.clarabridge.elasticsearch.index.migrate;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.bulk.BulkItemResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import java.io.Closeable;

class BulkWaiter implements BulkProcessor.Listener, Closeable {
    private static final Logger log = LoggerFactory.getLogger(BulkWaiter.class);

    private AtomicLong numBulks = new AtomicLong(0);
    private volatile boolean closed; // = false is by default

    private AtomicReference<BulkItemResponse.Failure> firstFailure = new AtomicReference<BulkItemResponse.Failure>();

    @Override
    public void close() {
        if (closed) {
            return;
        }
        closed = true;
        closeWait();
    }

    private synchronized void closeWait() {
        log.debug("start closing bulk processor");
        while (numBulks.get() != 0) {
            try {
                final long timeout = 1;
                log.info("waiting bulk processor for {} seconds", timeout);
                TimeUnit.SECONDS.sleep(timeout);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Override
    public void beforeBulk(long executionId, BulkRequest request) {
        numBulks.incrementAndGet();
        log.debug("beforeBulk");
    }

    @Override
    public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
        numBulks.decrementAndGet();
        for (BulkItemResponse bir : response) { // if (response.hasFailures()) {
            //String bulkFailureMessage = response.buildFailureMessage();
            BulkItemResponse.Failure failure = bir.getFailure();
            if (failure != null) {
                firstFailure.compareAndSet(null, failure);
                log.error("bulk failure id: {} status: {} msg: {}", failure.getId(), failure.getStatus(), failure.getMessage());
            }
        }
        log.debug("afterBulk completed");
    }

    @Override
    public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
        numBulks.decrementAndGet();
        log.error("afterBulk with failure", failure);
        // TODO: implement this handling also
        //throw new RuntimeException(failure.getMessage(), failure);
    } 

    public void checkErrors() throws Exception {
        BulkItemResponse.Failure failure = firstFailure.get();
        if (failure != null) {
            throw new Exception(failure.getMessage());
        }
    }
}
