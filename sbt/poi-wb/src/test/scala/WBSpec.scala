import org.scalatest._

//import kantan.csv.ops._

import java.io.File

import org.slf4j.Logger
import org.slf4j.LoggerFactory

//import scala.collection.JavaConversions._

class WBSpec extends FlatSpec {
    private val log = LoggerFactory.getLogger(classOf[WBSpec])

    "WB" should "read a spreadsheet" in {
        log.info("start")

        /*val out = new File("out.csv")
        val writer = out.asCsvWriter[(Int, String)](',', List("key", "val"))
        writer.write((1, "abc"))
        writer.write((2, "def"))
        writer.close()*/

        log.info("finish")
        assert(true === true)
    }
}

