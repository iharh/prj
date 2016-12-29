import org.scalatest._

import com.typesafe.config.ConfigFactory

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class ConfSpec extends FlatSpec {
    private val log = LoggerFactory.getLogger(getClass) // classOf[ConfSpec]

    "conf" should "match" in {
        log.info("start")

        val config = ConfigFactory.load()
        assert("value" === config.getString("config.value"))

        log.info("end")
    }
}
