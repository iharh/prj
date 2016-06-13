import io.gatling.core.Predef._
import io.gatling.http.Predef._
//import io.gatling.core.session.Expression
import io.gatling.http.{ HeaderNames, HeaderValues }
import io.gatling.http.request.builder.HttpRequestBuilder

import io.gatling.http.funspec.GatlingHttpFunSpec

//import org.slf4j.{Logger, LoggerFactory}

class ProcessMultiVerbatimSpec extends GatlingHttpFunSpec {
    //private final val log: Logger = LoggerFactory.getLogger(classOf[GatlingHttpFunSpec])

    override val baseURL = "http://localhost:18080"

    def pageHeader = css("h12")

    private final val text = "We booked through Priceline, which initially caused some doubts about our upcoming vacation. As Priceline only guarantees a king-size room, we ran into a brick wall when we called the hotel directly to ask to switch to a double (as a family of four, we have used Priceline many times and have never had a problem with Marriott or with Hilton Garden Inn). The staff on that call was extremely rude and unhelpful. Priceline gave us no satisfaction, either, so we headed to DC not knowing what to expect. However, upon check-in the staff could not have been more accomodating to our request -- we even got the room on a non-smoking floor. The hotel's proximity to restaurants and the White House is great, but the Metro station immediately across the street is the best ~feature~. We spent 7 days in DC and either walked or took the Metro everywhere we went (with the exception of one day spent on the Tourmobile to get us into Arlington National Cemetary). There is also a park 1/2 block away, so our boys could play catch in the early evening. The CVS pharmacy/convenience store 3 blocks away also helped us cut costs by allowing us to purchase breakfast and snack items to keep in the room. We did not use the restaurants in the hotel, but did take advantage of the small convenience kiosk in the lobby and the business center to check e-mail, etc. The concierge staff was very helpful with directions, and the two gentlemen who worked the afternoon desk were always in good humor. We would definitely book this hotel again for any length of stay in Washington."

    def bodyPMV(prjName: String, verbText: String): scala.xml.Elem = {
        <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:real="http://realtime.cbapi.clarabridge.com/">
            <soapenv:Header/>
            <soapenv:Body>
                <real:processMultiVerbatimDocument>
                    <processMultiVerbatimDocumentRequest>
                        <projectName>{prjName}</projectName>
                        <responseLevel>FULL</responseLevel>
                        <limitByWordRank>false</limitByWordRank>
                        <includeNRelations>true</includeNRelations>
                        <save dupDetection="NONE">false</save>
                        <verbatimSet>
                            <verbatim type="REVIEW">{verbText}</verbatim>
                        </verbatimSet>
                        <limitByWordRank>true</limitByWordRank>
                        <includeNRelations>false</includeNRelations>
                        <include_world_awareness>true</include_world_awareness>
                    </processMultiVerbatimDocumentRequest>
                </real:processMultiVerbatimDocument>
            </soapenv:Body>
        </soapenv:Envelope>
    }

    def myStuff(name: String, url: String): HttpRequestBuilder = http(name + " test")
            .post(url)
            .basicAuth("admin", "admin")
            //.asXML
            .header(HeaderNames.ContentType, HeaderValues.TextXml) //.header("Content-Type", "text/xml; charset=utf-8")

    spec {
        myStuff("processMultiVerbatim", "/cbapi/realtime?wsdl")
            .body(StringBody(bodyPMV("en1", text).toString))

            .check(status.is(200))
    }
}
