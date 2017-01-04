import org.scalatest._

import monix.eval.Task

import monix.reactive.{Observable, Observer, Consumer}

import monix.execution.Ack.Continue
import monix.execution.Cancelable
import monix.execution.Scheduler.Implicits.global

import kantan.csv.CsvWriter
import kantan.csv.ops._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

import com.typesafe.config.ConfigFactory

import java.io.{BufferedReader, StringReader, FileReader, File}

import org.slf4j.LoggerFactory

class TxtToCSVSpec extends FlatSpec with Matchers {
    private val log = LoggerFactory.getLogger(getClass)

    def writeRecord(writer: CsvWriter[(String)], text: String): Unit = {
        log.info("text: {}", text)
        writer.write((text))
    }
        
    "twit" should "write" in {
        log.info("start")

        val config = ConfigFactory.load()
        //val modelDirName = config.getString("ld.model.dir.name")

        //val lng = Language.Spanish
        val lngCode = "zh" // lng.toString()

        val out = new File(s"out/${lngCode}.csv")
        val writer = out.asCsvWriter[(String)](',', "text") // List("text")

        // addidas, lenovo, apple, intel, android, samsung, google, microsoft
        // reebok, sony
        val awaitable = Observable
            .fromLinesReader(new BufferedReader(new FileReader(s"d:\\dev\\prj\\sbt\\twit4s\\${lngCode}\\${lngCode}.txt")))
            // Consumer.complete
            .consumeWith(Consumer.foreach { writeRecord(writer, _) })
            .runAsync

        Await.result(awaitable, Duration.Inf) // 0 nanos
        writer.close()

        log.info("end")
    }
}
