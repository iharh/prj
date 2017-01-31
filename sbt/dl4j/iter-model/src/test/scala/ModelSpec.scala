import org.scalatest._

//import scala.collection.JavaConverters._

import java.io.File

import org.slf4j.LoggerFactory

class ModelSpec extends FlatSpec with Matchers {
    private val log = LoggerFactory.getLogger(getClass)

    "model" should "be-fine" in {
        log.info("start")

        log.info("end")
    }
}
