import org.scalatest._

import twitter4j.Twitter
import twitter4j.TwitterFactory
import twitter4j.conf.ConfigurationBuilder
import twitter4j.Query
import twitter4j.QueryResult
import twitter4j.Status
import twitter4j.TwitterResponse
import twitter4j.HttpResponseCode
import twitter4j.auth.AccessToken
import twitter4j.TwitterException

import kantan.csv.CsvWriter
import kantan.csv.ops._

//import monix.eval.Task

import monix.reactive.Observable
import monix.reactive.Observer
import monix.reactive.Consumer

//import monix.execution.Ack
import monix.execution.Ack.Continue
import monix.execution.Cancelable
import monix.execution.Scheduler.Implicits.global

import scala.collection.JavaConverters._

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

    case class TwitSearchState(twitter: Twitter, query: Query)

    def checkTwitterResult(resp: TwitterResponse) {
        val rls = resp.getRateLimitStatus
        if (rls != null && rls.getRemaining == 0) {
            val waitSec = rls.getSecondsUntilReset + 15
            log.info("Waiting {} seconds", waitSec)
            // TODO: try Scala Await
            Thread.sleep(waitSec * 1000)
        }
    }

    def searchTweets(s: TwitSearchState) : (Seq[Status], TwitSearchState) = {
        val query = s.query
        val twitter = s.twitter
        if (query == null) {
            (Seq.empty[Status], TwitSearchState(twitter, query))
        } else {
            val qr: QueryResult = twitter.search(query)
            checkTwitterResult(qr)
            val tweets = qr.getTweets // List[Status]
            //log.info("portion maxId: {}, hasNext: {}", qr.getMaxId().toString, qr.hasNext().toString: Any)
            (tweets.asScala, TwitSearchState(s.twitter, qr.nextQuery))
        }
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

    def observableForQuery(twitter: Twitter, langCode: String, query: String): Observable[Seq[Status]] = {
        var q = new Query(query)
        q.setLang(langCode)
        q.setCount(100)
        Observable.fromStateAction(searchTweets)(TwitSearchState(twitter, q)).takeWhile { !_.isEmpty }
    }

    "twit" should "search" in {
        log.info("start")

        val config = ConfigFactory.load()
        //config.entrySet().asScala.foreach { log.info("e: {}", _) }

	val cb = new ConfigurationBuilder()
	val twitter: Twitter = new TwitterFactory(cb.build()).getInstance()

        val consumerKey = config.getString("twitter.consumer.key")
        val consumerSecret = config.getString("twitter.consumer.secret")
        val accessKey = config.getString("twitter.access.key")
        val accessSecret = config.getString("twitter.access.secret")

	twitter.setOAuthConsumer(consumerKey, consumerSecret)
	val accessToken = new AccessToken(accessKey, accessSecret)
	twitter.setOAuthAccessToken(accessToken)

        val modelDirName = config.getString("ld.model.dir.name")

        val langDetector: NormLangDetector = getLangDetector(modelDirName);

        val langCode = "ro"

        val out = new File(s"out/${langCode.toString()}.csv")
        val writer = out.asCsvWriter[(String)](',', "text") // List("text")

        val queries = Seq(
            "addidas", "lenovo", "apple", "intel", "android", "samsung", "google", "microsoft",
            "reebok", "sony", "columbia", "audi", "hilton", "mozilla", "BMW", "renault", "taiwan",
            "pilsner", "carlsberg", "bavaria", "shalke", "real madrid",
            "volkswagen", "mercedes", "shalke 04",
            "milan", "juventus"
        )

        val awaitable = Observable.fromIterable(queries)
            .concatMap { observableForQuery(twitter, langCode, _) }
            .concatMap { Observable.fromIterable(_) } // Seq[Status] => Observable[Status]
            .filter { _.getLang() == langCode }
            .filter { t: Status => detectLang(langDetector, t.getText()) != langCode }
            .filter { t: Status => hasHashtagOrMention(t.getText()) }
            .map { _.getText() }
            .distinct
            .take(1000)
            // Consumer.complete
            .consumeWith(Consumer.foreach { writeTweetText(writer, _) })
            .runAsync

        Await.result(awaitable, Duration.Inf) // 0 nanos
        writer.close()

        assert(true == true)
    }
}
