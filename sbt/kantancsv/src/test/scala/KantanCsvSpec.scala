import org.scalatest._

//import java.util.List

import org.slf4j.Logger
import org.slf4j.LoggerFactory

//import scala.collection.JavaConversions._

class KantanCsvSpec extends FlatSpec {
    private val log = LoggerFactory.getLogger(classOf[KantanCsvSpec])

    "KantanCsv" should "write a sample file" in {
        log.info("start")
        log.info("finish")
        assert(true === true)
    }
}
