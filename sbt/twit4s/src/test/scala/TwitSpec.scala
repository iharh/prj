import org.scalatest._

import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.entities.Tweet

import com.danielasfregola.twitter4s.entities.enums.ResultType
import com.danielasfregola.twitter4s.entities.enums.Language

import monix.eval.Task

import monix.reactive.Observable
import monix.reactive.Observer
import monix.reactive.Consumer

import monix.execution.Ack
import monix.execution.Ack.Continue
import monix.execution.Cancelable
import monix.execution.Scheduler.Implicits.global

import scala.concurrent.Future
import scala.concurrent.Await
//import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

import org.slf4j.Logger
import org.slf4j.LoggerFactory

class TwitSpec extends FlatSpec with Matchers {
    private val log = LoggerFactory.getLogger(getClass)

    val client = TwitterRestClient()

    case class TwitSearchState(query: String, lang: Language.Value, max_id: Option[Long] = None)

    def searchTweets(s: TwitSearchState) : Task[(Seq[Tweet], TwitSearchState)] = {
        def extractNextMaxId(params: Option[String]): Option[Long] = {
            //example: "?max_id=658200158442790911&q=%23scala&include_entities=1&result_type=mixed"
            params.getOrElse("").split("&").find(_.contains("max_id")).map(_.split("=")(1).toLong)
        }

        val f1 = client.searchTweet(s.query, count = 10, language = Some(s.lang), result_type = ResultType.Recent, max_id = s.max_id)
            .flatMap { result => // case class StatusSearch(statuses: List[Tweet], search_metadata: SearchMetadata)
                val metadata = result.search_metadata
                val nextMaxId = extractNextMaxId(metadata.next_results)
                // metadata.count sometimes more than result.statuses.size

                val tweets = result.statuses
                if (tweets.nonEmpty) {
                    log.debug("found {} tweets", tweets.size)
                } else {
                    log.warn("empty tweets size")
                }
                Future { (tweets, TwitSearchState(s.query, s.lang, nextMaxId)) }
            //} recover {
            //    case _ => Seq.empty
            }

        Task.fromFuture(f1)
    }
        
    class TwitObserver(log: Logger) extends Observer.Sync[Seq[Tweet]] {
        def onNext(elem: Seq[Tweet]): Ack = {
            log.info("onNext: {}", elem)
            Continue
        }
        def onError(ex: Throwable) = {
            log.error("onError: {}", ex.toString) // : Any
            global.reportFailure(ex)
        }
        def onComplete() = {
            log.info("completed")
        }
    }

    "twit" should "search" in {
        log.info("start")

        val obs = Observable
            .fromAsyncStateAction(searchTweets)(TwitSearchState("addidas", Language.Spanish, None))
            // .take(5) // 5 portions in our case
            //.subscribe(new TwitObserver(log)) //   ---> Cancellable

        val f = obs
            .consumeWith(Consumer.complete)
            .runAsync

        Await.result(f, Duration.Inf) // 0 nanos

        //val res = searchTweets("addidas", Language.Spanish, 100).map { tweets =>
        //    log.info("Downloaded {} tweets", tweets.size)
        //    tweets.foreach { tweet => log.info("text: {}", tweet.text) }
        //}
        //Await.result(res, Duration.Inf) // 0 nanos

        log.info("end")
    }
}
