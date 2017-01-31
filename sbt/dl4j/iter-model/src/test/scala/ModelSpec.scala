import org.scalatest._

//import scala.collection.JavaConverters._

import org.deeplearning4j.models.word2vec.Word2Vec
import org.deeplearning4j.models.word2vec.VocabWord
import org.deeplearning4j.models.embeddings.inmemory.InMemoryLookupTable
import org.deeplearning4j.models.word2vec.wordstore.VocabCache
import org.deeplearning4j.models.word2vec.wordstore.inmemory.InMemoryLookupCache

import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.ops.transforms.Transforms

import java.io.File
import java.io.InputStream
//import java.io.IOException

import org.slf4j.LoggerFactory

class ModelSpec extends FlatSpec with Matchers {
    private val log = LoggerFactory.getLogger(getClass)

    "model" should "be-fine" in {
        log.info("start")

        log.info("end")
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
