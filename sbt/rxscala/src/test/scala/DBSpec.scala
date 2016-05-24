import org.scalatest._

import rx.functions.Action1

import com.github.davidmoten.rx.jdbc.Database
import com.github.davidmoten.rx.jdbc.tuple.Tuple2
import com.github.davidmoten.rx.jdbc.tuple.Tuple4

import kantan.csv.ops._

import java.io.File
//import java.util.List

import org.slf4j.Logger
import org.slf4j.LoggerFactory

//import scala.collection.JavaConversions._


class DBSpec extends FlatSpec {
    private val log = LoggerFactory.getLogger(classOf[DBSpec])
    private val listProjWithPwdSqlQuery = """select
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


    private def listProjWithPwd(db: Database): Unit =
        db
            .select(listProjWithPwdSqlQuery)
            .getAs(classOf[Integer], classOf[String], classOf[String], classOf[String])
            .subscribe(new Action1[Tuple4[Integer, String, String, String]] {
                override def call(p: Tuple4[Integer, String, String, String]): Unit = 
                    log.info("{} -> {} login: {} pwd: {}", p.value1(), p.value2(), p.value3(), p.value4()) 
            })

    private def listExportTokens(db: Database, fileN: String, pred: String): Unit = {
        val listExportTokensSqlQuery = s"""select
    ms.ms_token_name as WORD,
    ms.sentiment as SENTIMENT
from
    P_MS_TOKEN ms
left outer join
    PU_POSITIVE_NEGATIVE pn
on
    pn.WORD = ms.MS_TOKEN_NAME
where
    $pred
order by
    WORD"""

        val out = new File("out" + File.separator + fileN + ".csv")
        val writer = out.asCsvWriter[(String, Int)](',', List("key", "val")) // problems with Integer
        db
            .select(listExportTokensSqlQuery)
            .getAs(classOf[String], classOf[Integer])
            .toBlocking()
            .subscribe(new Action1[Tuple2[String, Integer]] {
                override def call(p: Tuple2[String, Integer]): Unit =
                    writer.write((p.value1(), p.value2(): Int))
            })
        // full - 1322
        writer.close()
    }

    "DB" should "get sentiment export" in {
        //log.info("start")
        def db =
            //DBUtils.getOraConfDB("tangerine6")
            DBUtils.getOraProcessingDB("tangerine6", "Gap Ratings Reviews")

        //listProjWithPwd(db)
        listExportTokens(db, "out", "(pn.SENTIMENT is null and ms.sentiment <> 0) or (pn.SENTIMENT <> ms.sentiment)")
        listExportTokens(db, "new_non_zero", "pn.SENTIMENT is null and ms.sentiment <> 0")
        listExportTokens(db, "modified", "pn.SENTIMENT <> ms.sentiment")

        //log.info("end")
        assert(true === true)
    }
}
