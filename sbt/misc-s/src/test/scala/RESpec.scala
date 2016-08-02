import org.scalatest._
import org.scalatest.prop.TableDrivenPropertyChecks._

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

    val table = Table(
        ("cnt", "text"),
        (9    , "當客戶有問題查詢時"),
        (2    , "豐a樂bb"),
        (1    , "挙c"),
        (2    , "a六d鳥"),
        (1    , "eedf妎asdf1"),
        (2    , "企畫sdfsdf"),
        (1    , "a猙"),
        (2    , "籠手")
    )

    "RE" should "match stuff" in {
        log.info("start")

        forAll (table) { (cnt, text) => assert(cnt === getCJKCharCnt(text)) }

        log.info("end")
    }
}

