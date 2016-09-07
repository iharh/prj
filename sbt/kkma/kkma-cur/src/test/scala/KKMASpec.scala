import org.scalatest._

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class KKMASpec extends FlatSpec {
    private val log = LoggerFactory.getLogger(classOf[KKMASpec])

    "KKMA" should "do some dumb assert" in {
        log.info("start")
        log.info("end")

        assert(true === true)
    }
}
