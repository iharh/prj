import monix.reactive._

import monix.execution.Ack
import monix.execution.Ack.Continue

import scala.collection.JavaConverters._

import org.snu.ids.ha.ma.MorphemeAnalyzer
import org.snu.ids.ha.ma.Tokenizer;
import org.snu.ids.ha.ma.Token;

import org.slf4j.Logger

class KKMAObserver(log: Logger) extends Observer.Sync[String] {

    private var line = 0
    private val ma = new MorphemeAnalyzer

    def processTok(elem: String) {
        val tokens = Tokenizer.tokenize(elem)
        tokens.asScala.foreach { t: Token => log.info("{}", t.toString()) } // idx str charset
    }

    def processMA(elem: String) {
        val mexp = ma.analyze(elem)
        val bestMexp = ma.leaveJustBest(mexp)
        val sentences = ma.divideToSentences(bestMexp)
    }

    def onNext(elem: String): Ack = {
        line += 1
        log.info("processing {}: {}", line.toString, elem: Any)

        //processMA(elem)
        processTok(elem)

        Continue
    }

    def onError(ex: Throwable) = {
        log.error("{}: {}", line.toString, ex.toString: Any)
        line += 1
    }

    def onComplete() = {
        log.info("{}: completed", line.toString)
        line += 1
    }
}
