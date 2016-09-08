import org.scalatest._

//import monix.eval._
import monix.reactive._

import monix.execution.Scheduler.Implicits.global

import org.snu.ids.ha.ma.Token;
import org.snu.ids.ha.ma.Tokenizer;

import java.io.BufferedReader
import java.io.InputStreamReader

import java.nio.charset.StandardCharsets._

import org.slf4j.Logger
import org.slf4j.LoggerFactory


class KKMASpec extends FlatSpec {
    private val log = LoggerFactory.getLogger(classOf[KKMASpec]) // ??? .getClass()
    //private val TEXTS = List("나라마다", "abc");

    def getR(resName: String): BufferedReader =
        new BufferedReader(
            new InputStreamReader(
                classOf[KKMASpec].getResourceAsStream("/" + resName),
                UTF_8)
        )

    "KKMA" should "do some dumb assert" in {
        log.info("start")

        Observable
            .fromLinesReader(getR("lines.txt")) // .fromIterable(TEXTS.seq) // 0 until 10   .range(0, 10)
            .subscribe(new LogObserver("item", log))

        log.info("end")

        assert(true === true)
    }
}
