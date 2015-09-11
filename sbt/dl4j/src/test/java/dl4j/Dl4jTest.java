package dl4j;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;


import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;
import org.deeplearning4j.models.word2vec.wordstore.inmemory.InMemoryLookupCache;

import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.ops.transforms.Transforms;

//import rx.Observable;
import static rx.observables.StringObservable.from;
import static rx.observables.StringObservable.byLine;


import org.apache.commons.compress.compressors.gzip.GzipUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.IOException;
//import java.io.;
import java.util.List;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;


public class Dl4jTest {
    private static final Logger log = LoggerFactory.getLogger(Dl4jTest.class);

    private static final int MAX_SIZE = 50;

    private static final int TEST_ITERATIONS = 100000;

    @Test
    public void testDl4j() throws Exception {
        java.lang.Thread.currentThread().setContextClassLoader(
            java.lang.ClassLoader.getSystemClassLoader()
        );

        //INDArray nd = Nd4j.create(new double[]{1, 2, 3, 4}, new int[]{2, 2});
        //
        final List<String> lines = byLine(
            from(new FileReader("en-words-100.txt"))
        )
        .toList()
        .toBlocking()
        .single();

        log.info("lines size: {}", lines.size());

        log.info("start loading model");

        String modelFileName = "D:/clb/src/spikes/cb-cps/templates/word2vecModels_bck/GoogleNews";

        Word2Vec w2v = readBinaryModel(new File(modelFileName));
        assertNotNull(w2v);

        log.info("finish loading model");
        
        for (int i = 0; i < TEST_ITERATIONS; ++i) {
            for (String word : lines)
            {
                log.debug("start iteration: {} word: {}", i, word);
                w2v.wordsNearest(word, 10);
            }
        }

        log.info("done");
        assertTrue(true);
    }

    private static Word2Vec readBinaryModel(File modelFile) throws NumberFormatException, IOException {
        InMemoryLookupTable lookupTable;
        VocabCache cache;
        INDArray syn0;

        try (BufferedInputStream bis = new BufferedInputStream(
                GzipUtils.isCompressedFilename(modelFile.getName()) ?
                    new GZIPInputStream(new FileInputStream(modelFile)) :
                    new FileInputStream(modelFile)
             );
             DataInputStream dis = new DataInputStream(bis)) {

            int words = Integer.parseInt(readString(dis));
            int size = Integer.parseInt(readString(dis));
            syn0 = Nd4j.create(words, size);
            cache = new InMemoryLookupCache(false);
            lookupTable = (InMemoryLookupTable) new InMemoryLookupTable.Builder()
                .cache(cache)
                .vectorLength(size)
                .build();

            float [] vector = new float[size];
            String word;

            for (int i = 0; i < words; ++i) {
                word = readString(dis);
                if (cache.wordFrequency(word) > 0) {
                    throw new IllegalArgumentException("Duplicate word has been found, word: " + word + ", lineNumber: " + i);
                }

                for (int j = 0; j < size; ++j) {
                    vector[j] = readFloat(dis);
                }

                syn0.putRow(i, Transforms.unitVec(Nd4j.create(vector)));

                cache.addWordToIndex(cache.numWords(), word);
                cache.addToken(new VocabWord(1, word));
                cache.putVocabWord(word);
            }
        }

        Word2Vec ret = new Word2Vec(); // new CustomWord2Vec();
        lookupTable.setSyn0(syn0);
        ret.setVocab(cache);
        ret.setLookupTable(lookupTable);
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

    private static byte [] readStringBytes(DataInputStream dis) throws IOException {
        List<Byte> bytes = new ArrayList<>();
        byte b = dis.readByte();
        while (b != 32 && b != 10) {
            bytes.add(b);
            b = dis.readByte();
        }
        byte [] result = new byte[bytes.size()];
        int i = 0;
        for (Byte bb : bytes) {
            result[i++] = bb;
        }
        return result;
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
}

