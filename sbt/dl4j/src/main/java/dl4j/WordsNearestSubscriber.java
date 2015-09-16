package dl4j;

import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;

import org.nd4j.linalg.api.ndarray.INDArray;

import rx.Subscriber;


import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import java.util.PriorityQueue;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordsNearestSubscriber extends Subscriber<String> {
    private static final Logger log = LoggerFactory.getLogger(WordsNearestSubscriber.class);

    public static final long DIVIDER = 1000000L;
    private static final ThreadMXBean THREAD_MXBEAN = ManagementFactory.getThreadMXBean();

    private static final int TOP_SIZE = 10;
    
    
    private CountDownLatch finish;
    private String p;
    private InMemoryLookupTable lookupTable;
    private VocabCache vocabCache;
    private INDArray syn0transposed;

    private PriorityQueue<Long> execQueue;
    private PriorityQueue<Long> cpuQueue;

    public WordsNearestSubscriber(CountDownLatch finish, String p, InMemoryLookupTable lookupTable, VocabCache vocabCache, INDArray syn0transposed) {
        this.finish = finish;
        this.p = p;
        this.lookupTable = lookupTable;
        this.vocabCache = vocabCache;
        this.syn0transposed = syn0transposed;

        execQueue = new PriorityQueue<Long>();
        cpuQueue = new PriorityQueue<Long>();
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
    public void onNext(String word) {
        long exec = System.nanoTime();
        long cpu = THREAD_MXBEAN.getCurrentThreadCpuTime();

        Utils.wordsNearestNew(lookupTable, vocabCache, syn0transposed, word, TOP_SIZE);

        exec = System.nanoTime() - exec;
        cpu = THREAD_MXBEAN.getCurrentThreadCpuTime() - cpu;

        PriorityQueue<Double[]> queue = new PriorityQueue<>(vec.rows(),comparator);

        // auto-boxing
        execQueue.add(exec); 
        cpuQueue.add(cpu); 

        log.info("onNext {} word: {} exec: {}ms cpu: {}ms", p, word, exec / DIVIDER, cpu / DIVIDER);
    }

    public long getExecTime() {
        int s = execQueue.size();
        int skipFirst = s / 10;
        int skipLast = s / 10;


    }

    public long getCpuTime() {
        return cpuQueue.size();
    }
};
