import org.scalatest._

//import kantan.csv.ops._
import org.apache.poi.ss.util._
import org.apache.poi.ss.usermodel._
import org.apache.poi.xssf.usermodel._

import java.io.File

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._
//import scalaj.collection.Imports._

class WBSpec extends FlatSpec with Matchers {
    private val log = LoggerFactory.getLogger(classOf[WBSpec])

    "WB" should "read a spreadsheet" in {
        log.info("start")

        val sheetName = "Spanish Taxonomy with Sentences"
        val inFile = new File("in" + File.separator + sheetName + ".xlsx")
        val wb = new XSSFWorkbook(inFile)

        val sheet = wb.getSheetAt(0)
        sheet should not be (null)

        //val i = sheet.rowIterator().asScala // for scalaj
        val i = asScalaIterator(sheet.rowIterator())
        i should not be (null)

        i.foreach(r => onRow(r))

        log.info("finish")
    }

    private val IDX_NODE_NAME     = 1
    private val IDX_PREBUILT_RULE = 4
    private val IDX_WORD_COUNT    = 9

    def onRow(row: Row): Unit = {
        val firstN = row.getFirstCellNum()
        val lastN  = row.getLastCellNum()

        if (firstN <= IDX_NODE_NAME && IDX_WORD_COUNT <= lastN) {
            val cNodeName = row.getCell(IDX_NODE_NAME)
            val cPrebuiltRule = row.getCell(IDX_PREBUILT_RULE)
            //val cWordCount = row.getCell(IDX_WORD_COUNT)

            if (isStr(cNodeName) && isStr(cPrebuiltRule) /*&& isNum(cWordCount)*/) {
                val nodeName = cNodeName.getStringCellValue()
                val prebuiltRule = cPrebuiltRule.getStringCellValue()
                //val wordCount = cWordCount.getNumericCellValue()
                // row.getRowNum()
                log.info("Name: {} Rule: {}", nodeName, prebuiltRule: Any)
                //log.info("Word cnt: {}", wordCount)
            }
        }
    }

    def isStr(cell: Cell): Boolean = {
        cell.getCellType() == Cell.CELL_TYPE_STRING
    }
    def isNum(cell: Cell): Boolean = {
        cell.getCellType() == Cell.CELL_TYPE_NUMERIC
    }
}

