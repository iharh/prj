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

    private def listProj(db: Database): Unit = {
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
    }

    private def listProjWithPwd(db: Database): Unit = {
        def sqlQuery = """select
    pj.id, pj.name,
    ds.username, ds.pwd
from
    cb_project pj,
    cb_datflow_ds_xref dx,
    cb_datasource ds
where
    pj.id = dx.project_id
    and dx.id_datasource = ds.id
    and dx.id_conntype = 1
order by pj.name"""

        val projects = db
            .select(sqlQuery)
            .getAs(classOf[Integer], classOf[String], classOf[String], classOf[String])
            .toList()
            .toBlocking()
            .single()

        log.info("start")
        projects.foreach(p => //: Tuple2<Integer, String>
            log.info("{} -> {} login: {} pwd: {}", p.value1(), p.value2(), p.value3(), p.value4()) 
        )
        log.info("end")
    }

    "DB" should "get project list" in {
        def db = DBUtils.getDb()

        //listProj(db)
        listProjWithPwd(db)

        assert(true === true)
    }
}
