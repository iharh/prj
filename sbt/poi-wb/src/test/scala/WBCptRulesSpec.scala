import org.scalatest._

import org.apache.poi.ss.util._
import org.apache.poi.ss.usermodel._
import org.apache.poi.xssf.usermodel._

import org.apache.commons.lang3.StringUtils

import org.json4s._
import org.json4s.jackson.JsonMethods._

import com.fasterxml.jackson.databind.ObjectWriter

import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._

class WBCptRulesSpec extends FlatSpec with Matchers {
    private val log = LoggerFactory.getLogger(classOf[WBCptRulesSpec])

    private object XlsxWriter {
        private val outWb = new XSSFWorkbook()
        private val outSheet = outWb.createSheet("samples")
        private val firstRow: XSSFRow = outSheet.createRow(0)
        firstRow.createCell(0).setCellValue("id") // XSSFCell
        firstRow.createCell(1).setCellValue("verb")

        private var rowNum: Int = 1

        def writeSent(sent: String) {
            val row = outSheet.createRow(rowNum)
            row.createCell(0).setCellValue(rowNum)
            row.createCell(1).setCellValue(sent)
            rowNum += 1;
        }

        def done() = {
            val fileOut = new FileOutputStream("out" + File.separator + "r.xlsx")
            outWb.write(fileOut)
            fileOut.flush()
            fileOut.close()
        }
    }

    "WBTaxo" should "read a spreadsheet" in {
        log.info("start")

        val sheetName = "Spanish Concept Rules with Sentences"
        val inFile = new File("in" + File.separator + sheetName + ".xlsx")
        val inWb = new XSSFWorkbook(inFile)

        val inSheet = inWb.getSheetAt(0)
        inSheet should not be (null)

        val itr:Iterator[Row] = asScalaIterator(inSheet.rowIterator())
        itr should not be (null)

        itr
            .map(mapRow(_))
            .map(_.trim())
            .filter(!_.isEmpty())
            .foreach(XlsxWriter.writeSent(_))

        XlsxWriter.done()

        log.info("finish")
    }

    private val IDX_SAMPLE_SENT = 1

    def mapRow(row: Row): String = {
        val firstN = row.getFirstCellNum()
        val lastN  = row.getLastCellNum()

        if (firstN <= IDX_SAMPLE_SENT && IDX_SAMPLE_SENT <= lastN) {
            val cSampleSent = row.getCell(IDX_SAMPLE_SENT)

            if (isStr(cSampleSent)) {
                // row.getRowNum()
                val sent = cSampleSent.getStringCellValue()
                log.info("sample: {}", sent)

                sent
            } else {
                ""
            }
        } else {
            ""
        }
    }

    def isStr(cell: Cell): Boolean = {
        cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING
    }
}
