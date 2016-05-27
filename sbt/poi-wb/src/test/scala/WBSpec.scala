import org.scalatest._

//import kantan.csv.ops._
import org.apache.poi.ss.util._
import org.apache.poi.xssf.usermodel._

import java.io.File

import org.slf4j.Logger
import org.slf4j.LoggerFactory

//import scala.collection.JavaConversions._

class WBSpec extends FlatSpec with Matchers {
    private val log = LoggerFactory.getLogger(classOf[WBSpec])

    "WB" should "read a spreadsheet" in {
        log.info("start")

        val inFile = new File("in" + File.separator + "Taxonomies.xlsx")
        val wb = new XSSFWorkbook(inFile)

        val sheet = wb.getSheetAt(0)
        sheet should not be (null)

        /*val out = new File("out.csv")
        val writer = out.asCsvWriter[(Int, String)](',', List("key", "val"))
        writer.write((1, "abc"))
        writer.write((2, "def"))
        writer.close()*/

        log.info("finish")
    }
}

