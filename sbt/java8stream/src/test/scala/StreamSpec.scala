import org.scalatest._

import grizzled.slf4j.Logging

import java.util.{List => JList, ArrayList}
import java.util.stream.Collectors

import scala.collection.JavaConverters._

class WordAndVector(word: String, id: Int) {
    def getWord: String = word
    def getId: Int = id
    override def toString = s"{id: $id, word: $word}"
}

object WordAndVector {
    def apply(word: String, id: Int) = new WordAndVector(word, id)

    def sampleList(): JList[WordAndVector] = {
        val t1 = WordAndVector("tok1", 1)
        val t2 = WordAndVector("tok2", 2)

        //val tokens: JList[WordAndVector] = List(t1, t2).asJava
        val result: JList[WordAndVector] = new ArrayList[WordAndVector]();
        result.add(t1);
        result.add(t2);

        result
    }
}

class StreamSpec extends FlatSpec with Matchers with Logging {
    "stream" should "be fine" in {
        info("start")

        val tokens = WordAndVector.sampleList()

        info(s"tokens: $tokens class: ${tokens.getClass}")
    }
}
