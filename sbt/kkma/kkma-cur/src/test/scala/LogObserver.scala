import monix.reactive._

import monix.execution.Ack
import monix.execution.Ack.Continue

import org.slf4j.Logger

class LogObserver[-A](prefix: String, log: Logger) extends Observer.Sync[A] {

    private[this] var pos = 0

    def onNext(elem: A): Ack = {
      log.info("{}: {}-->{}", pos.toString(), prefix, elem.toString())
      pos += 1
      Continue
    }

    def onError(ex: Throwable) = {
      log.error("{}: {}-->{}", pos.toString(), prefix, ex.toString())
      pos += 1
    }

    def onComplete() = {
      log.info("{}: {} completed", pos.toString(), prefix: Any)
      pos += 1
    }
}
