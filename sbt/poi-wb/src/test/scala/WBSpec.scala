import org.scalatest._

//import kantan.csv.ops._
import org.apache.poi.ss.util._
import org.apache.poi.ss.usermodel._
import org.apache.poi.xssf.usermodel._

import org.apache.commons.lang3.StringUtils

import org.json4s._
//import org.json4s.native.JsonMethods._ // UTF-8 problems here
import org.json4s.jackson.JsonMethods._

import java.io.File
import java.io.FileOutputStream
//import java.io.StringWriter
import java.io.PrintWriter

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._
//import scalaj.collection.Imports._

class WBSpec extends FlatSpec with Matchers {
    private val log = LoggerFactory.getLogger(classOf[WBSpec])

    private object XlsxWriter {
        private val outWb = new XSSFWorkbook()
        private val outSheet = outWb.createSheet("samples")
        private val firstRow = outSheet.createRow(0) // XSSFRow
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
            val fileOut = new FileOutputStream("out" + File.separator + "o.xlsx")
            outWb.write(fileOut)
            fileOut.flush()
            fileOut.close()
        }
    }

    "WB" should "read a spreadsheet" in {
        log.info("start")

        val sheetName = "Spanish Taxonomy with Sentences"
        //val sheetName = "Taxonomies"
        val inFile = new File("in" + File.separator + sheetName + ".xlsx")
        val inWb = new XSSFWorkbook(inFile)

        val inSheet = inWb.getSheetAt(0)
        inSheet should not be (null)

        //val itr = inSheet.rowIterator().asScala // for scalaj
        val itr:Iterator[Row] = asScalaIterator(inSheet.rowIterator())
        itr should not be (null)


        val oList: List[JField] = itr
            .map(mapRow(_))
            .filter(_._2 != JNothing)
            .filter(_._1 != "Node Name")
            .toList

        val o = JObject(List(
            "Language"     -> JString("es"),
            "Taxonomy Name"-> JString("CB Universe (Std)"),
            "Word Bucket"  -> JObject(oList)
        ))
        val d = render(o)

        //val res = pretty(r)
        //val w = new StringWriter
        val w = new PrintWriter(new File("out" + File.separator + "o.json"))
        val objWriter = mapper.writerWithDefaultPrettyPrinter() // com.fasterxml.jackson.databind.ObjectWriter
        objWriter.writeValue(w, d)

        //log.info("{}", w.toString)
        XlsxWriter.done()

        log.info("finish")
    }

    private val IDX_NODE_NAME     = 1
    private val IDX_PREBUILT_RULE = 4
    private val IDX_SAMPLE_SENT   = 9

    def mapRow(row: Row): JField = {
        val firstN = row.getFirstCellNum()
        val lastN  = row.getLastCellNum()

        if (firstN <= IDX_NODE_NAME && IDX_PREBUILT_RULE <= lastN) {
            val cNodeName = row.getCell(IDX_NODE_NAME)
            val cPrebuiltRule = row.getCell(IDX_PREBUILT_RULE)
            val cSampleSent = row.getCell(IDX_SAMPLE_SENT)

            if (isStr(cNodeName) && isStr(cPrebuiltRule)) {
                // row.getRowNum()
                val nodeName = cNodeName.getStringCellValue()
                val prebuiltRule = cPrebuiltRule.getStringCellValue()

                val jWords = prebuiltRule.split(",")
                    .map(_.trim)
                    .map(s => new JString(StringUtils.strip(s, "\"")))
                    .toList

                if (isStr(cSampleSent)) {
                    val sent = cSampleSent.getStringCellValue()
                    log.info("sample: {}", sent)
                    XlsxWriter.writeSent(sent)
                }

                JField(nodeName, JArray(jWords)) // (nodeName -> JArray(jWords))
            } else {
                JField("", JNothing)
            }
        } else {
            JField("", JNothing)
        }
    }

    def isStr(cell: Cell): Boolean = {
        cell != null && cell.getCellType() == Cell.CELL_TYPE_STRING
    }
    //def isNum(cell: Cell): Boolean = {
    //    cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC
    //}
}
