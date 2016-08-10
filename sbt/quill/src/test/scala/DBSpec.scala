import org.scalatest._

import io.getquill._

//import java.io.File
//import java.util.List

import org.slf4j.Logger
import org.slf4j.LoggerFactory

//import scala.collection.JavaConversions._

case class CbProject(id: Long, name: String)

class DBSpec extends FlatSpec {
    private val log = LoggerFactory.getLogger(classOf[DBSpec])

    def dodo = {
        //lazy val ctx = new JdbcContext[PostgresDialect, PostgresEscape]("db")
        lazy val ctx = new JdbcContext[PostgresDialect, SnakeCase]("db")

        import ctx._

        //val q = quote {
        //    query[CbProject]
        //}
        ctx.run(quote(query[CbProject]))
    }

    "DB" should "do some dumb assert" in {
        log.info("start")

        dodo

        log.info("end")

        assert(true === true)
    }
}
