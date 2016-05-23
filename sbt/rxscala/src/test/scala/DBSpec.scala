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

    "DB" should "get sentiment export" in {
        //getOraConfDB("tangerine6")
        def db = DBUtils.getOraProcessingDB("tangerine6", "Gap Ratings Reviews")

        //listProjWithPwd(db)
/*
        val cnt = db
            .select("select count(*) from p_ms_token")
            .getAs(classOf[Integer])
            .take(1)
            .toBlocking()
            .single()
        log.info("ms tokens cnt: {}", cnt)
*/
        def sqlQuery = """select
    ms.ms_token_name as WORD,
    ms.sentiment as SENTIMENT
from
    P_MS_TOKEN ms
left outer join
    PU_POSITIVE_NEGATIVE pn
on
    pn.WORD = ms.MS_TOKEN_NAME
where
    (pn.SENTIMENT is null and ms.sentiment <> 0)
    or (pn.SENTIMENT <> ms.sentiment)
order by
    WORD"""

        val items = db
            .select(sqlQuery)
            .getAs(classOf[String], classOf[Double])
            .toList()
            .toBlocking()
            .single()

        items.foreach(i =>
            log.info("{} -> {}", i.value1(), i.value2()) 
        )
        // full - 1322
    
        assert(true === true)
    }
}
