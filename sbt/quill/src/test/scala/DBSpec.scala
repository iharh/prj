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

    def getProjects: List[CbProject] = {
        //PostgresEscape
        lazy val ctx = new JdbcContext[PostgresDialect, SnakeCase]("db")

        import ctx._

        //ctx.run(quote(query[CbProject]))
        val q = quote {
            query[CbProject]
        }
        ctx.run(q)
    }

    "DB" should "do some dumb assert" in {
        log.info("start")
        getProjects.foreach(p => log.info("id: {} name: {}", p.id, p.name))
        log.info("end")

        assert(true === true)
    }
}
