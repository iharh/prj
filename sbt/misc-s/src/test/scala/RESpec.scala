import org.scalatest._

import java.util.regex.{Pattern, Matcher}

import org.slf4j.{LoggerFactory, Logger}

class RESpec extends FlatSpec {
    private val log = LoggerFactory.getLogger(classOf[RESpec])

    private val pat = Pattern.compile("\\p{sc=Han}")

    def getCJKCharCnt(text: String) = {
        val m: Matcher = pat.matcher(text);
        var result = 0
        // ? splitAsStream - http://www.java2s.com/Tutorials/Java_Streams/Tutorial/Streams/Streams_from_Regex.htm 
        while (m.find()) {
            result += 1
        }
        result
    }

    "RE" should "match stuff" in {
        log.info("start")

        assert(9 === getCJKCharCnt("當客戶有問題查詢時"))

        log.info("end")
    }
}

