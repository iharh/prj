package dl4j;

import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;

import org.nd4j.linalg.api.ndarray.INDArray;

import rx.Observable;
import rx.schedulers.Schedulers;

import static rx.observables.StringObservable.from;
import static rx.observables.StringObservable.byLine;


import java.io.File;
import java.io.Reader;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dl4jRun {
    private static final Logger log = LoggerFactory.getLogger(Dl4jRun.class);
    //private static final int TEST_ITERATIONS = 100000;
    private static final int TOP_SIZE = 10;

    private static final String warmUpFileName = "en-words-warmup.txt";
    private static final String wordsFileName = "en-words.txt";

    public static void main(String [] args) {
        try {
            //checkBackends();
            if (args.length < 2) {
                throw new Exception("missing model file name and/or num threads");
            }

            final String modelFileName = args[0];
            int numThreads = Integer.parseInt(args[1]);
            log.info("params modelFileName: {} numThreads: {}", modelFileName, numThreads);

            final Reader wordsReader = getReaderCP(wordsFileName);
            final List<String> wordLines = byLine(from(wordsReader))
                .toList().toBlocking().single();

            int maxSize = wordLines.size();
            log.info("lines size: {}", maxSize);

            final Observable<String> baseObservable = Observable.from(wordLines)
                .subscribeOn(Schedulers.computation());


            log.info("start loading model: {}", modelFileName);
            final Word2Vec w2v = UtilsRead.readBinaryModel(new File(modelFileName));
            final InMemoryLookupTable lookupTable = (InMemoryLookupTable) w2v.lookupTable();
            final VocabCache vocabCache = w2v.vocab();
            log.info("finish loading model and start transposing");
            
            final INDArray syn0 = lookupTable.getSyn0();
            syn0.diviRowVector(syn0.norm2(1));
            final INDArray syn0transposed = syn0.transpose();
            log.info("finish transposing model");

            warmUp(lookupTable, vocabCache, syn0transposed, TOP_SIZE);

            for (int i = 0; i < numThreads; ++i) {
                testWordsNearest(i + 1, baseObservable, maxSize, lookupTable, vocabCache, syn0transposed, TOP_SIZE);
            }

            log.info("done");
        } catch (Exception e) {
            log.error("Caught Exception: " + e.getMessage(), e);
            System.exit(1);
        }
    }

    private static Reader getReaderCP(String resFileName) {
        //new FileReader(resFileName);
        return new BufferedReader(new InputStreamReader(Dl4jRun.class.getResourceAsStream("/" + resFileName)));
    }

    private static void warmUp(InMemoryLookupTable lookupTable, VocabCache vocabCache, INDArray syn0transposed, int topSize) {
        final Reader warmupReader = getReaderCP(warmUpFileName);
        final List<String> warmupLines = byLine(from(warmupReader))
            .toList().toBlocking().single();

        int warmupSize = warmupLines.size();
        log.info("warmup size: {}", warmupSize);

        WordsNearestSubscriber warmupSubscriber = new WordsNearestSubscriber(warmupSize, null, "warmup", lookupTable, vocabCache, syn0transposed, topSize);
        Observable.from(warmupLines).subscribe(warmupSubscriber);

        warmupSubscriber.logStat();

        log.info("warmup done");
    }

    private static void testWordsNearest(int numThreads, final Observable<String> baseObservable, int maxSize
        , InMemoryLookupTable lookupTable, VocabCache vocabCache, INDArray syn0transposed, int topSize) {

        log.info("stat start test with threads: {}", numThreads);

        final CountDownLatch finish = new CountDownLatch(numThreads);

        WordsNearestSubscriber [] subscribers = new WordsNearestSubscriber[numThreads];
        for (int i = 0; i < numThreads; ++i) {
            WordsNearestSubscriber subscriber = new WordsNearestSubscriber(maxSize, finish, "s" + i, lookupTable, vocabCache, syn0transposed, topSize);
            subscribers[i] = subscriber;
            baseObservable
                .subscribe(subscriber);
        }
        //log.info("done chain creating");
        try {
            finish.await();
        } catch (InterruptedException e) {
        }

        log.info("stat finish test with threads: {}", numThreads);

        //log.info("done waiting");
        for (int i = 0; i < numThreads; ++i) {
            subscribers[i].logStat();
        }
    }
}

