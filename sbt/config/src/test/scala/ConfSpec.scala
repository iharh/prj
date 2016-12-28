import org.scalatest._

import java.io.File

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ConfSpec extends FlatSpec {
    private val log = LoggerFactory.getLogger(classOf[ConfSpec])

    "conf" should "match" in {
        log.info("start")
        assert(true === true)
        log.info("end")
    }
}
