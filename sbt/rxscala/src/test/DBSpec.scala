import org.scalatest._

import com.github.davidmoten.rx.jdbc.Database
import com.github.davidmoten.rx.jdbc.tuple.Tuple2

import collection.mutable.Stack

import java.util.List

import org.slf4j.Logger
import org.slf4j.LoggerFactory


class DBSpec extends FlatSpec {
    private val log = LoggerFactory.getLogger(this.getClass.getName)

    "DB" should "get project list" in {
        def db = DBUtils.getDb()

        //List<Tuple2<Integer, String>>
        val projects = db
            .select("select id, name from cb_project")
            .getAs(classOf(Integer), classOf(String))
            .toList()
            .toBlocking()
            .single();

        /*log.info("start");
        for (Tuple2<Integer, String> p : projects) {
            log.info(p.value1() + " -> " + p.value2());
        }
        log.info("end");*/
// .select("select name from person where name > ? order by name")
// .parameter("ALEX")
        assert(true === true)
    }
}
