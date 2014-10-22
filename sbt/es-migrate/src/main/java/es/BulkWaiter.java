package es;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import java.io.Closeable;

class BulkWaiter implements BulkProcessor.Listener, Closeable {
    private static final Logger log = LoggerFactory.getLogger(BulkWaiter.class);

    private AtomicLong numBulks = new AtomicLong(0);
    private volatile boolean closed; // = false is by default

    @Override
    public void close() {
        if (closed) {
            return;
        }
        closed = true;
        closeWait();
    }

    private synchronized void closeWait() {
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
        log.info("beforeBulk");
    }

    @Override
    public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
        numBulks.decrementAndGet();
        log.info("afterBulk success");
    }

    @Override
    public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
        numBulks.decrementAndGet();
        log.error("afterBulk error", failure);
    } 
}
