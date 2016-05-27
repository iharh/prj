import org.scalatest._

import org.json4s._
import org.json4s.native.JsonMethods._

import java.io.File

import org.slf4j.Logger
import org.slf4j.LoggerFactory

//import scala.collection.JavaConversions._

class JSONSpec extends FlatSpec with Matchers {
    private val log = LoggerFactory.getLogger(classOf[JSONSpec])

    "JSON" should "write a string" in {
        var a: List[JString] = Nil
        a = JString("def") :: a
        a = JString("abc") :: a
        a should not be (null)

        var f: List[(String, JValue)] = Nil
        f = "postId" -> JInt(1) :: f
        f = "arr" -> JArray(a) :: f
        f should not be (null)

        val o = JObject(f)
        o should not be (null)

        val res = pretty(render(o))
        //log.info("res: {}", res)
    }
}
