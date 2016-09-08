import org.scalatest._

//import monix.eval._
import monix.reactive._

import monix.execution.Ack
import monix.execution.Ack.Continue

import monix.execution.Scheduler.Implicits.global

import org.slf4j.Logger
import org.slf4j.LoggerFactory


class KKMASpec extends FlatSpec {
    private val log = LoggerFactory.getLogger(classOf[KKMASpec])

    "KKMA" should "do some dumb assert" in {
        log.info("start")

        Observable
            .fromIterable(0 until 10)
            //.range(0, 10)
            .subscribe(new LogObserver("abc", log))

        log.info("end")

        assert(true === true)
    }

    private class LogObserver[-A](prefix: String, log: Logger) extends Observer.Sync[A] {

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
}
