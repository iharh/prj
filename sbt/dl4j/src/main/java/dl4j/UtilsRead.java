package dl4j;

import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.models.word2vec.VocabWord;
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable;
import org.deeplearning4j.models.word2vec.wordstore.VocabCache;
import org.deeplearning4j.models.word2vec.wordstore.inmemory.InMemoryLookupCache;

import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.ops.transforms.Transforms;

import org.apache.commons.compress.compressors.gzip.GzipUtils;

import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.io.DataInputStream;
import java.io.BufferedInputStream;
import java.io.IOException;

import java.util.zip.GZIPInputStream;

public class UtilsRead {

    public static Word2Vec readBinaryModel(File modelFile) throws NumberFormatException, IOException {
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

    private static final int MAX_SIZE = 50;

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
                bytes = new byte[MAX_SIZE]; // ??? why do we re-init here ???
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
};    
