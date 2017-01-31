import org.scalatest._

//import scala.collection.JavaConverters._

import resource._

import org.deeplearning4j.models.word2vec.Word2Vec
import org.deeplearning4j.models.word2vec.VocabWord
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable
import org.deeplearning4j.models.word2vec.wordstore.VocabCache
import org.deeplearning4j.models.word2vec.wordstore.inmemory.InMemoryLookupCache

import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.ops.transforms.Transforms

import org.apache.commons.compress.compressors.gzip.GzipUtils

import java.io.File
import java.io.InputStream
import java.io.DataInputStream
import java.io.FileInputStream
import java.io.BufferedInputStream
//import java.io.IOException

import java.util.zip.GZIPInputStream;

import org.slf4j.LoggerFactory

class ModelSpec extends FlatSpec with Matchers {
    private val log = LoggerFactory.getLogger(getClass)

    "model" should "be-fine" in {
        log.info("start")

        log.info("end")
    }
}

object ModelUtils {
    private val MAX_SIZE = 50

    def getBis(modelFile: File): BufferedInputStream = new BufferedInputStream(
        if (GzipUtils.isCompressedFilename(modelFile.getName())) {
            new GZIPInputStream(new FileInputStream(modelFile))
        } else {
            new FileInputStream(modelFile)
        });

    def readBinaryModel(modelFile: File): Word2Vec = { // throws NumberFormatException, IOException
        var ret: Word2Vec = new Word2Vec() // new CustomWord2Vec();

        for {
            bis <- managed(getBis(modelFile))
            dis <- managed(new DataInputStream(bis))
        } {
            val words:Int = Integer.parseInt(readString(dis))
            val size: Int = Integer.parseInt(readString(dis))
            var syn0: INDArray = Nd4j.create(words, size)
            var cache: VocabCache[VocabWord] = new InMemoryLookupCache(false)
            /*var lookupTable: InMemoryLookupTable[VocabWord] = new InMemoryLookupTable[VocabWord].Builder()
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
            lookupTable.setSyn0(syn0)
            ret.setVocab(cache)
            ret.setLookupTable(lookupTable)
            */
        }
        ret
    }

    def readString(dis: DataInputStream): String = { // throws IOException
        var bytes: Array[Byte] = new Array[Byte](MAX_SIZE)
        var b: Byte = dis.readByte()
        var i: Int = -1
        var sb: StringBuilder = new StringBuilder()
        while (b != 32 && b != 10) {
            i = i + 1
            bytes(i) = b
            b = dis.readByte()
            if (i == 49) {
                sb.append(new String(bytes))
                i = -1
                bytes = new Array[Byte](MAX_SIZE)
            }
        }
        sb.append(new String(bytes, 0, i + 1));
        sb.toString()
    }

    def readFloat(is: InputStream): Float = { // throws IOException
        var bytes: Array[Byte] = new Array[Byte](4)
        is.read(bytes)
        getFloat(bytes)
    }

    def getFloat(b: Array[Byte]): Float = {
        var accum: Int = 0
        accum = accum | (b(0) & 0xff) << 0
        accum = accum | (b(1) & 0xff) << 8
        accum = accum | (b(2) & 0xff) << 16
        accum = accum | (b(3) & 0xff) << 24
        java.lang.Float.intBitsToFloat(accum)
    }
}
