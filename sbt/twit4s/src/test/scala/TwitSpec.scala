import org.scalatest._

import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.entities.Tweet

import com.danielasfregola.twitter4s.entities.enums.ResultType
import com.danielasfregola.twitter4s.entities.enums.Language

import monix.eval.Task

import monix.reactive.Observable
import monix.reactive.Observer
import monix.reactive.Consumer

import monix.execution.Ack.Continue
import monix.execution.Cancelable
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Future
import scala.concurrent.Await
//import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

import org.slf4j.LoggerFactory

class TwitSpec extends FlatSpec with Matchers {
    private val log = LoggerFactory.getLogger(getClass)

    val client = TwitterRestClient()

    case class TwitSearchState(query: String, lang: Language.Value, maxId: Option[Long] = None)

    def searchTweets(s: TwitSearchState) : Task[(Seq[Tweet], TwitSearchState)] = {
        def extractNextMaxId(params: Option[String]): Option[Long] = {
            //example: "?max_id=658200158442790911&q=%23scala&include_entities=1&result_type=mixed"
            params.getOrElse("").split("&").find(_.contains("max_id")).map(_.split("=")(1).toLong)
        }

        val resultFuture = client.searchTweet(s.query, count = 10, language = Some(s.lang), result_type = ResultType.Recent, max_id = s.maxId)
            .flatMap { result => // case class StatusSearch(statuses: List[Tweet], search_metadata: SearchMetadata)
                val metadata = result.search_metadata
                // metadata.count sometimes more than result.statuses.size

                val tweets = result.statuses
                if (tweets.nonEmpty) {
                    log.debug("found {} tweets", tweets.size)
                } else {
                    log.warn("empty tweets size")
                }

                val nextMaxId = extractNextMaxId(metadata.next_results)
                Future { (tweets, TwitSearchState(s.query, s.lang, nextMaxId)) }
            //} recover {
            //    case _ => Seq.empty
            }

        Task.fromFuture(resultFuture)
    }
        
    "twit" should "search" in {
        log.info("start")

        val awaitable = Observable
            .fromAsyncStateAction(searchTweets)(TwitSearchState("addidas", Language.Spanish))
            .concatMap { Observable.fromIterable(_) } // Seq[Tweet] => Observable[Tweet]
            .filter { _.lang == Some(Language.Spanish.toString()) }
            .take(33)
            // Consumer.complete
            .consumeWith(Consumer.foreach { t: Tweet => log.info("lang: {}, text: {}", t.lang, t.text: Any) })
            .runAsync

        Await.result(awaitable, Duration.Inf) // 0 nanos

        log.info("end")
    }
}
