import org.scalatest._

import com.github.davidmoten.rx.jdbc.Database
import com.github.davidmoten.rx.jdbc.tuple.Tuple2

import collection.mutable.Stack

import java.util.List

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import scala.collection.JavaConversions._


class DBSpec extends FlatSpec {
    private val log = LoggerFactory.getLogger(classOf[DBSpec])

    "DB" should "get project list" in {
        def db = DBUtils.getDb()

        // projects: List<Tuple2<Integer, String>>
        val projects = db
            .select("select id, name from cb_project")
            .getAs(classOf[Integer], classOf[String])
            .toList()
            .toBlocking()
            .single()

        log.info("start")
        projects.foreach(p => //: Tuple2<Integer, String>
            log.info(p.value1() + " -> " + p.value2()) 
        )
        log.info("end")

        assert(true === true)
    }
}
