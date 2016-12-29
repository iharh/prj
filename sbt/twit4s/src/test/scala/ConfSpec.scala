import org.scalatest._

import com.typesafe.config.ConfigFactory

import org.slf4j.LoggerFactory

class ConfSpec extends FlatSpec with Matchers {
    private val log = LoggerFactory.getLogger(getClass)

    "conf" should "match" in {
        log.info("start")

        val config = ConfigFactory.load()
        config.getString("twitter.access.key") should not be empty

        log.info("end")
    }
}
