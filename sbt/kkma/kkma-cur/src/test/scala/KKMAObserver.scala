import monix.reactive._

import monix.execution.Ack
import monix.execution.Ack.Continue

import org.snu.ids.ha.ma.MorphemeAnalyzer
//import org.snu.ids.ha.ma.Token;
//import org.snu.ids.ha.ma.Tokenizer;

import org.slf4j.Logger

class KKMAObserver(log: Logger) extends Observer.Sync[String] {

    private var line = 0
    private val ma = new MorphemeAnalyzer

    def onNext(elem: String): Ack = {
        line += 1
        log.info("processing {}: {}", line.toString, elem: Any)

        val mexp = ma.analyze(elem)
        val bestMexp = ma.leaveJustBest(mexp)
        val sentences = ma.divideToSentences(bestMexp)

        //Tokenizer.tokenize(elem)

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
