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

import kantan.csv.CsvWriter
import kantan.csv.ops._

//import scala.collection.JavaConverters._

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

import com.typesafe.config.ConfigFactory

import java.util.regex.Matcher
import java.util.regex.Pattern

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
        def extractNextSinceId(params: Option[String]): Option[Long] = {
            params.getOrElse("").split("&").find(_.contains("since_id")).map(_.split("=")(1).toLong)
        }
        def extractNextResults(params: Option[String]): Option[Boolean] = {
            params.getOrElse("").split("&").find(_.contains("next_results")).map(_.split("=")(1).toBoolean)
        }

        val resultFuture = s.client.searchTweet(s.query, count = 100, language = Some(s.lang), result_type = ResultType.Recent, max_id = s.maxId)
            .flatMap { result => // case class StatusSearch(statuses: List[Tweet], search_metadata: SearchMetadata)
                val metadata = result.search_metadata
                // metadata.count sometimes more than result.statuses.size

                val tweets = result.statuses
                //if (tweets.nonEmpty) { log.debug("found {} tweets", tweets.size) } else { log.warn("empty tweets size") }

                val nextMaxId = extractNextMaxId(metadata.next_results)
                val nextSinceId = extractNextSinceId(metadata.next_results)
                val nextResults = extractNextResults(metadata.next_results)

                log.info("portion prev_max_id: {} next_max_id: {}, next_since_id: {}, next_results: {}",
                    s.maxId.toString, nextMaxId.toString, nextSinceId.toString, nextResults.toString)

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

    def detectLang(langDetector: NormLangDetector, text: String): String = { // throws IOException, ModelCreatorException, MathException
        val sourceIterator: StringSourceIterator = new StringSourceIterator(text, text.length());
        val res: Result = langDetector.analyse(sourceIterator); // throws ModelCreatorException, MathException
        if (res.getConfidenceLevel() > DESIRED_CONFIDENCE_LEVEL) res.getLangCode() else "un";
    }

    val oldURLS = Pattern.compile(
        "(?:[\\p{Nd}a-z\\._\\-]+@[\\p{Nd}a-z\\._\\-]+)|" //email
        + "(?:(?:(?:ht|f)tp(?:s?)\\:\\/\\/|~\\/|\\/)?" //protocol
        + "(?:\\w+:\\w+@)?" //Username:Password@
        + "(([a-z0-9\\-\\.]+\\.(com|org|net|gov|mil|biz|info|mobi|name|aero|jobs|museum|travel|[a-z]{2}))|" //$NON-NLS-1$ // Domains with subdomains
        + "(?:(?:\\d{1,3}\\.){3}\\d{1,3}))" //Top level domain as IP address
        + "(?::[\\d]{1,5})?" //Port
        + "([^\\s]*))" //$NON-NLS-1$ //Directories queries and anchors
        , Pattern.CASE_INSENSITIVE
    )

    val newPat = Pattern.compile(
        "(?:[#|@][\\p{Nd}A-Za-z_0-9]+)" //hashtags and mentions
        , Pattern.CASE_INSENSITIVE
    )

    def hasHashtagOrMention(text: String): Boolean = {
        val oldMatcher: Matcher = oldURLS.matcher(text)
        val filteredText = oldMatcher.replaceAll("")

        val newMatcher: Matcher = newPat.matcher(filteredText)
        newMatcher.find()
    }

    def writeTweetText(writer: CsvWriter[(String)], text: String): Unit = {
        log.info("text: {}", text)
        writer.write((text))
    }
        
    "twit" should "search" in {
        log.info("start")

        val client = TwitterRestClient()

        val config = ConfigFactory.load()
        //config.entrySet().asScala.foreach { log.info("e: {}", _) }
        val modelDirName = config.getString("ld.model.dir.name")

        val langDetector: NormLangDetector = getLangDetector(modelDirName);

        // Spanish ChineseSimplified
        val lng = Language.ChineseSimplified
        val lngStr = "fr" // lng.toString()

        val out = new File(s"out/${lng.toString()}.csv")
        val writer = out.asCsvWriter[(String)](',', "text") // List("text")

        // addidas, lenovo, apple, android, samsung, google, microsoft
        // intell, dell, logitech
        // sony, panasonic
        // reebok, columbia, audi, hilton, mozilla, BMW
        // taiwan, renault, opel
        // docker, vmware
        // lego
        // java
        val awaitable = Observable
            .fromAsyncStateAction(searchTweets)(TwitSearchState(client, "lego", lng))
            .concatMap { Observable.fromIterable(_) } // Seq[Tweet] => Observable[Tweet]
            .filter { _.lang == Some(lngStr) }
            .map { _.text }
            .distinct
            .filter { detectLang(langDetector, _) != lngStr }
            .filter { hasHashtagOrMention(_) }
            .take(1000)
            // Consumer.complete
            .consumeWith(Consumer.foreach { writeTweetText(writer, _) })
            .runAsync

        Await.result(awaitable, Duration.Inf) // 0 nanos
        writer.close()

        log.info("end")
    }
}
