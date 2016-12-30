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


import com.clarabridge.transformer.ld.CrossModel
import com.clarabridge.transformer.ld.NormLangDetector
import com.clarabridge.transformer.ld.StringSourceIterator
import com.clarabridge.transformer.ld.NormCrossModelScorer.Result
import com.clarabridge.transformer.ld.compiler.Compiler
import com.clarabridge.transformer.ld.utils.FileNamesCollector
//import com.clarabridge.transformer.ld.exceptions.ModelCreatorException
//import com.clarabridge.transformer.ld.exceptions.MathException

import java.io.File

import org.slf4j.LoggerFactory

class TwitSpec extends FlatSpec with Matchers {
    private val log = LoggerFactory.getLogger(getClass)

    case class TwitSearchState(client: TwitterRestClient, query: String, lang: Language.Value, maxId: Option[Long] = None)

    def searchTweets(s: TwitSearchState) : Task[(Seq[Tweet], TwitSearchState)] = {
        def extractNextMaxId(params: Option[String]): Option[Long] = {
            //example: "?max_id=658200158442790911&q=%23scala&include_entities=1&result_type=mixed"
            params.getOrElse("").split("&").find(_.contains("max_id")).map(_.split("=")(1).toLong)
        }

        val resultFuture = s.client.searchTweet(s.query, count = 10, language = Some(s.lang), result_type = ResultType.Recent, max_id = s.maxId)
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
                Future { (tweets, TwitSearchState(s.client, s.query, s.lang, nextMaxId)) }
            //} recover {
            //    case _ => Seq.empty
            }

        Task.fromFuture(resultFuture)
    }

    // private static final
    val DESIRED_CONFIDENCE_LEVEL: Double = 0.01; // 0.5;

    def getLangDetector(modelDirName: String): NormLangDetector = { // throws IOException, ModelCreatorException
        val modelCollector = new FileNamesCollector();

        val modelDir = new File(modelDirName);
        log.debug("Loading language detector models from \"{}\"", modelDir.getCanonicalPath());

        modelCollector.addFileMask(modelDir, "*.ldm");
        log.debug("Found {} model(s)", modelCollector.getFiles().size());

        val crossModel: CrossModel = Compiler.createModel(modelCollector.getFiles());
        new NormLangDetector(crossModel, DESIRED_CONFIDENCE_LEVEL);
    }

    def detect(langDetector: NormLangDetector, text: String): String = { // throws IOException, ModelCreatorException, MathException
        val sourceIterator: StringSourceIterator = new StringSourceIterator(text, text.length());
        val res: Result = langDetector.analyse(sourceIterator); // throws ModelCreatorException, MathException
        if (res.getConfidenceLevel() > DESIRED_CONFIDENCE_LEVEL) res.getLangCode() else "un";
    }
        
    "twit" should "search" in {
        log.info("start")

        val client = TwitterRestClient()

        val awaitable = Observable
            .fromAsyncStateAction(searchTweets)(TwitSearchState(client, "addidas", Language.Spanish))
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
