import org.scalatest._

import grizzled.slf4j.Logging
//import org.slf4j.Logger
//import org.slf4j.LoggerFactory

class StreamSpec extends FlatSpec with Matchers with Logging {
    "stream" should "be fine" in {
        info("start")
    }
}
