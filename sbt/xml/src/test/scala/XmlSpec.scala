import org.scalatest._

//import java.io.File

import org.slf4j.Logger
import org.slf4j.LoggerFactory

//import scala.collection.JavaConversions._

class XmlSpec extends FlatSpec with Matchers {
    private val log = LoggerFactory.getLogger(classOf[XmlSpec])

    def theXml(name: String) = 
        <xmlMsg addressedTo={ name }>
            Hello, { name }!
        </xmlMsg>;

    "XML" should "write a string" in {
        val res = "Hello"
        res should not be (null)

        log.info("res: {}", theXml(res))
    }
}
