import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.http.funspec.GatlingHttpFunSpec

//object GatlingFunSpecExample {
//    def pageHeader = css("h1")
//}

class GatlingFunSpecExample extends GatlingHttpFunSpec {

    override val baseURL = "http://example.com"
    //override def httpConf = super.httpConf.header("MyHeader", "MyValue") // (3)

    def pageHeader = css("h12")

    spec {
        http("Example index.html test")
          .get("/index.html")
          .check(pageHeader.exists)
    }
}
