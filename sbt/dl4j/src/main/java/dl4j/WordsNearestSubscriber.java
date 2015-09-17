package dl4j;

import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;

import org.nd4j.linalg.api.ndarray.INDArray;

import rx.Subscriber;


import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.CountDownLatch;

import java.lang.reflect.Array;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordsNearestSubscriber extends Subscriber<String> {
    private static final Logger log = LoggerFactory.getLogger(WordsNearestSubscriber.class);

    public static final long DIVIDER = 1000000L;
    private static final ThreadMXBean THREAD_MXBEAN = ManagementFactory.getThreadMXBean();

    private int maxSize;
    private CountDownLatch finish;
    private String p;
    private InMemoryLookupTable lookupTable;
    private VocabCache vocabCache;
    private INDArray syn0transposed;
    private int topSize;

    private PriorityQueue<Long> execQueue;
    private PriorityQueue<Long> cpuQueue;

    private static class LongComparator implements Comparator<Long> {
        @Override
        public int compare(Long o1, Long o2) {
            return Double.compare(o1, o2);
        }
    }

    public WordsNearestSubscriber(int maxSize, CountDownLatch finish, String p
        , InMemoryLookupTable lookupTable, VocabCache vocabCache, INDArray syn0transposed, int topSize) {

        this.maxSize = maxSize;
        this.finish = finish;
        this.p = p;
        this.lookupTable = lookupTable;
        this.vocabCache = vocabCache;
        this.syn0transposed = syn0transposed;
        this.topSize = topSize;

        LongComparator comparator = new LongComparator();
        execQueue = new PriorityQueue<Long>(maxSize, comparator);
        cpuQueue = new PriorityQueue<Long>(maxSize, comparator);
    }

    @Override
    public void onCompleted() {
        log.info("onCompleted {}", p);
        if (finish != null) {
            finish.countDown();
        }
    }

    @Override
    public void onError(Throwable e) {
        log.error("onError");
    }

    @Override
    public void onNext(String word) {
        long exec = System.nanoTime();
        long cpu = THREAD_MXBEAN.getCurrentThreadCpuTime();

        UtilsNew.wordsNearestNew(lookupTable, vocabCache, syn0transposed, word, topSize);

        exec = System.nanoTime() - exec;
        cpu = THREAD_MXBEAN.getCurrentThreadCpuTime() - cpu;

        // auto-boxing
        execQueue.add(exec); 
        cpuQueue.add(cpu); 

        log.info("onNext {} word: {} exec: {}ms cpu: {}ms", p, word, exec / DIVIDER, cpu / DIVIDER);
    }

    public void logStat() {
        logStatQ("excec", execQueue);
        logStatQ("cpu", cpuQueue);
    }

    private void logStatQ(String qname, PriorityQueue<Long> queue) {
        //assert maxSize == queue.size();
        int skipFirst = maxSize / 10;
        int skipLast = skipFirst;

        log.info("stat maxSize: {} skipFirst: {} skipLast: {}", maxSize, skipFirst, skipLast);

        // first 10%
        long el = 0;
        //StringBuffer buf = new StringBuffer("{");
        for (int i = 0; i < skipFirst; ++i) {
            el = queue.poll(); // auto-unboxing
            //buf.append(" ").append(el);
        }

        // middle 80%
        long min = queue.peek(); // auto-unboxing
        //buf.append(" } - {");
        while (queue.size() > skipLast) {
            el = queue.poll(); // auto-unboxing
            //buf.append(" ").append(el);
        }
        long max = el;

        // last 10%
        //buf.append(" } - {");
        while (queue.size() > 0) {
            el = queue.poll(); // auto-unboxing
            //buf.append(" ").append(el);
        }
        //buf.append(" }");
        log.info("stat {} min: {} max: {}", qname, min, max);
    }
};
