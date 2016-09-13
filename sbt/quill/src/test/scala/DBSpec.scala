import org.scalatest._

import io.getquill._

//import java.io.File
//import java.util.List

import org.slf4j.Logger
import org.slf4j.LoggerFactory

//import scala.collection.JavaConversions._

case class CbProject(id: Long, name: String)

//-- CREATING TABLE CB_PROPERTIES --
//CREATE TABLE CB_PROPERTIES
//(
//  PROP_NAME  CHARACTER VARYING(255) NOT NULL,
//  PROP_VALUE CHARACTER VARYING(4000) NULL,
//  ID_PROJECT NUMERIC(20,0) DEFAULT 0 NOT NULL,
//  CONSTRAINT PK_CB_PROPERTIES PRIMARY KEY (PROP_NAME, ID_PROJECT)
//)
case class CbProperties(propName: String, propValue: String)

class DBSpec extends FlatSpec {
    private val log = LoggerFactory.getLogger(classOf[DBSpec])

    //PostgresEscape
    def getCtx: JdbcContext[PostgresDialect, SnakeCase] = new JdbcContext[PostgresDialect, SnakeCase]("db")

    def getProjects: List[CbProject] = {
        lazy val ctx = getCtx
        import ctx._

        //ctx.run(quote(query[CbProject]))
        val q = quote {
            query[CbProject]
        }
        ctx.run(q)
    }

    def getProperties: List[CbProperties] = {
        lazy val ctx = getCtx
        import ctx._

        val q = quote {
            query[CbProperties]
        }
        ctx.run(q)
    }

    "DB" should "do some query" in {
        log.info("start")
        //getProjects.foreach(p => log.info("id: {} name: {}", p.id, p.name))
        getProperties.foreach(p => log.info("propName: {} propValue: {}", p.propName, p.propValue: Any))
        log.info("end")

        assert(true === true)
    }
}
