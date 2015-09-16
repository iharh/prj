package dl4j;

import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;
import org.deeplearning4j.models.word2vec.wordstore.inmemory.InMemoryLookupCache;

import org.nd4j.linalg.factory.Nd4jBackend;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.ops.transforms.Transforms;

import rx.Observable;
import rx.schedulers.Schedulers;

import static rx.observables.StringObservable.from;
import static rx.observables.StringObservable.byLine;


import org.apache.commons.compress.compressors.gzip.GzipUtils;



import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.IOException;
//import java.io.;
import java.util.ServiceLoader;
import java.util.ServiceConfigurationError;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Dl4jRun {
    private static final Logger log = LoggerFactory.getLogger(Dl4jRun.class);

    private static final String MODEL_FILE_NAME =
        //"D:/clb/src/spikes/cb-cps/templates/word2vecModels_bck/GoogleNews";
        //"D:/clb/src/spikes/cb-cps/templates/word2vecModels_bck/GoogleNews_f[41]";
        "F:/w2v/GoogleNews";

    
    private static final int MAX_SIZE = 50;
    
    //private static final int TEST_ITERATIONS = 100000;
    private static final int NUM_THREADS = 3;

    public static void main(String [] args) {
        try {
            //checkBackends();

            final List<String> lines = byLine(
                from(new FileReader("en-words-100.txt"))
            )
            .toList()
            .toBlocking()
            .single();


            log.info("lines size: {}", lines.size());

            final Observable<String> baseObservable = Observable.from(lines)
                .subscribeOn(Schedulers.computation());


            log.info("start loading model: {}", MODEL_FILE_NAME);
            final Word2Vec w2v = readBinaryModel(new File(MODEL_FILE_NAME));
            final InMemoryLookupTable lookupTable = (InMemoryLookupTable) w2v.lookupTable();
            final VocabCache vocabCache = w2v.vocab();
            log.info("finish loading model and start transposing");
            
            final INDArray syn0 = lookupTable.getSyn0();
            syn0.diviRowVector(syn0.norm2(1));
            final INDArray syn0transposed = syn0.transpose();
            log.info("finish transposing model");

            testWordsNearest(NUM_THREADS, baseObservable, lookupTable, vocabCache, syn0transposed);
            
            /*for (int i = 0; i < TEST_ITERATIONS; ++i) {
                for (String word : lines)
                {
                    log.debug("start iteration: {} word: {}", i, word);
                    //INDArray words = w2v.getWordVectorMatrix(word);
                    //w2v.wordsNearest(words, TOP_SIZE);

                    //w2v.wordsNearest(word, TOP_SIZE);

                    //wordsNearestOld(lookupTable, vocabCache, word, TOP_SIZE);
                    //Utils.wordsNearestNew(lookupTable, vocabCache, syn0transposed, word, TOP_SIZE);
                }
            }*/

            log.info("done");
        } catch (Exception e) {
            log.error("Caught Exception: " + e.getMessage(), e);
        }
    }

    private static void testWordsNearest(int numThreads, final Observable<String> baseObservable, InMemoryLookupTable lookupTable, VocabCache vocabCache, INDArray syn0transposed) {
        final CountDownLatch finish = new CountDownLatch(numThreads);

        WordsNearestSubscriber [] subscribers = new WordsNearestSubscriber[numThreads];
        for (int i = 0; i < numThreads; ++i) {
            WordsNearestSubscriber subscriber = new WordsNearestSubscriber(finish, "s" + i, lookupTable, vocabCache, syn0transposed);
            subscribers[i] = subscriber;
            baseObservable
                .subscribe(subscriber);
        }

        //for (int i = 0; i < numThreads; ++i) {
        //}

        log.info("done chain creating");
        try {
            finish.await();
        } catch (InterruptedException e) {
        }
        log.info("done waiting");
    }


    private static Word2Vec readBinaryModel(File modelFile) throws NumberFormatException, IOException {
        Word2Vec ret = new Word2Vec(); // new CustomWord2Vec();

        try (BufferedInputStream bis = new BufferedInputStream(
                GzipUtils.isCompressedFilename(modelFile.getName()) ?
                    new GZIPInputStream(new FileInputStream(modelFile)) :
                    new FileInputStream(modelFile)
             );
             DataInputStream dis = new DataInputStream(bis)) {

            int words = Integer.parseInt(readString(dis));
            int size = Integer.parseInt(readString(dis));

            final INDArray syn0 = Nd4j.create(words, size);
            VocabCache vocabCache = new InMemoryLookupCache(false);
            final InMemoryLookupTable lookupTable = (InMemoryLookupTable) new InMemoryLookupTable.Builder()
                .cache(vocabCache)
                .vectorLength(size)
                .build();
            lookupTable.setSyn0(syn0);
            ret.setVocab(vocabCache);
            ret.setLookupTable(lookupTable);

            float [] vector = new float[size];
            String word;

            for (int i = 0; i < words; ++i) {
                word = readString(dis);
                if (vocabCache.wordFrequency(word) > 0) {
                    throw new IllegalArgumentException("Duplicate word has been found, word: " + word + ", lineNumber: " + i);
                }

                for (int j = 0; j < size; ++j) {
                    vector[j] = readFloat(dis);
                }

                syn0.putRow(i, Transforms.unitVec(Nd4j.create(vector)));

                vocabCache.addWordToIndex(vocabCache.numWords(), word);
                vocabCache.addToken(new VocabWord(1, word));
                vocabCache.putVocabWord(word);
            }
        }
        return ret;
    }

    private static String readString(DataInputStream dis) throws IOException {
        byte[] bytes = new byte[MAX_SIZE];
        byte b = dis.readByte();
        int i = -1;
        StringBuilder sb = new StringBuilder();
        while (b != 32 && b != 10) {
            ++i;
            bytes[i] = b;
            b = dis.readByte();
            if (i == 49) {
                sb.append(new String(bytes));
                i = -1;
                bytes = new byte[MAX_SIZE];
            }
        }
        sb.append(new String(bytes, 0, i + 1));
        return sb.toString();
    }

    private static float readFloat(InputStream is) throws IOException {
        byte[] bytes = new byte[4];
        is.read(bytes);
        return getFloat(bytes);
    }

    private static float getFloat(byte[] b) {
        int accum = 0;
        accum = accum | (b[0] & 0xff) << 0;
        accum = accum | (b[1] & 0xff) << 8;
        accum = accum | (b[2] & 0xff) << 16;
        accum = accum | (b[3] & 0xff) << 24;
        return Float.intBitsToFloat(accum);
    }

    private static void checkBackends() {
        final String cn = "org.nd4j.linalg.netlib.NetlibBlasBackend";
        Class<?> c = null;
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            c = Class.forName(cn, false, cl);
            log.error("Provider {} found", cn);
        } catch (ClassNotFoundException x) {
            log.error("Provider {} not found", cn);
        }

        List<Nd4jBackend> backends = new ArrayList<>(1);
        ServiceLoader<Nd4jBackend> loader = ServiceLoader.load(Nd4jBackend.class);
        try {

            Iterator<Nd4jBackend> backendIterator = loader.iterator();
            while (backendIterator.hasNext()) {
                Nd4jBackend b = backendIterator.next();
                //backends.add();
                log.info("Loaded: {}", b.getClass().getName()); // getSimpleName());
            }

        } catch (ServiceConfigurationError serviceError) {
            log.error("ServiceConfigurationError: " + serviceError.getMessage(), serviceError);
            // a fatal error due to a syntax or provider construction error.
            // backends mustn't throw an exception during construction.
            throw new RuntimeException("failed to process available backends", serviceError);
        }
    }

    private static Collection<String> wordsNearestOld(InMemoryLookupTable lookupTable, VocabCache vocabCache, String word, int top) {
        List<String> ret = new ArrayList<>();

        if (!vocabCache.containsWord(word)) {
            return ret;
        }

        INDArray words = Nd4j.create(1, lookupTable.layerSize());
        words.putRow(0, lookupTable.vector(word));

        INDArray mean = words.isMatrix() ? words.mean(0) : words;

        INDArray syn0 = lookupTable.getSyn0();

        INDArray weights = mean;
        INDArray distances = syn0.mmul(weights.transpose());
        distances.diviRowVector(distances.norm2(1));
        INDArray[] sorted = Nd4j.sortWithIndices(distances, 0, false);
        INDArray sort = sorted[0];

        int sortLen = sort.length();
        log.info("sort.length(): {}", sortLen);
        log.info("sort.offset(): {}", sort.offset());

        logIntArr("sort.stride", sort.stride());
        logIntArr("sort.shape", sort.shape());

        for (int i = 0; i < sortLen; ++i) {
            log.info("iter: {}", i);
            int idx = sort.getInt(i);
            log.info("idx: {}", idx);
        }

        if (top > sortLen)
            top = sortLen;
        //??? there will be a redundant word
        int end = top; // + 1;
        for (int i = 0; i < end; ++i) {
            int idx = sort.getInt(i);
            String w = vocabCache.wordAtIndex(idx);
            if (word.equals(w)) {
                ++end;
                if (end >= sortLen)
                    break;
                continue;
            }
            ret.add(w);
        }

        return ret;
    }

    private static void logIntArr(String prefix, int [] arr) {
        int len = arr.length;
        StringBuffer sb = new StringBuffer(prefix);
        sb.append(" len: ");
        sb.append(Integer.toString(len));
        sb.append(" {");
        for (int i = 0; i < len; ++i) {
            sb.append(" ");
            sb.append(Integer.toString(arr[i]));
        }
        sb.append(" }");
        log.info("{}", sb.toString());
    }
}

