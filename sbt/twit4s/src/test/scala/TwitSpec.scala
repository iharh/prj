import org.scalatest._

import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.entities.Tweet

import com.danielasfregola.twitter4s.entities.enums.ResultType
import com.danielasfregola.twitter4s.entities.enums.Language

import scala.concurrent.Await
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.duration._

import org.slf4j.LoggerFactory

class TwitSpec extends FlatSpec with Matchers {
    private val log = LoggerFactory.getLogger(getClass)

    val client = TwitterRestClient()

    def searchTweets(query: String, lang: Language.Value, count: Int, max_id: Option[Long] = None): Future[Seq[Tweet]] = {
        def extractNextMaxId(params: Option[String]): Option[Long] = {
          //example: "?max_id=658200158442790911&q=%23scala&include_entities=1&result_type=mixed"
          params.getOrElse("").split("&").find(_.contains("max_id")).map(_.split("=")(1).toLong)
        }

        if (count > 0) {
            client
                .searchTweet(query, count = 100, language = Some(lang), result_type = ResultType.Recent, max_id = max_id)
                .flatMap { result => // case class StatusSearch(statuses: List[Tweet], search_metadata: SearchMetadata)
                    val metadata = result.search_metadata
                    val nextMaxId = extractNextMaxId(metadata.next_results)
                    // metadata.count sometimes more than result.statuses.size

                    val tweets = result.statuses
                    if (tweets.nonEmpty) {
                        log.debug("found {} tweets", tweets.size)
                        searchTweets(query, lang, count - tweets.size, nextMaxId).map(_ ++ tweets)
                    } else {
                        log.warn("empty tweets size")
                        Future(tweets) // .sortBy(_.created_at)
                    }
                //} recover {
                //    case _ => Seq.empty
                }
        } else {
            log.warn("search excluded for count: {}", count)
            Future(Seq.empty)
        }
    }

    "twit" should "search" in {
        log.info("start")

        val res = searchTweets("addidas", Language.Spanish, 1000).map { tweets =>
            log.info("Downloaded {} tweets", tweets.size)
            tweets.foreach { tweet => log.info("text: {}", tweet.text) }
        }
        Await.result(res, Duration.Inf) // 0 nanos

        log.info("end")
    }
}
