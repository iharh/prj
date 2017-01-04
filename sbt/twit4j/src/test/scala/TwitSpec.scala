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

    case class TwitSearchState(twitter: Twitter, query: String, langCode: String, maxId: Long = -1l)

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
        var q = new Query(s.query)
        q.setLang(s.langCode)
        q.setMaxId(s.maxId)
        q.setCount(100)

        val qr: QueryResult = s.twitter.search(q)
        checkTwitterResult(qr)
        val tweets = qr.getTweets() // List[Status]

        val newMaxId = tweets.get(0).getId() // qr.getMaxId()
        log.info("portion oldMaxId: {}, newMaxId: {}, hasNext: {}", s.maxId.toString, newMaxId.toString, qr.hasNext().toString)

        (tweets.asScala, TwitSearchState(s.twitter, s.query, s.langCode, newMaxId))
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

    def writeTweetText(text: String): Unit = {
        log.info("text: {}", text)
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

        //val modelDirName = config.getString("ld.model.dir.name")

        //val langDetector: NormLangDetector = getLangDetector(modelDirName);

        // addidas, lenovo, apple, intel, android, samsung, google, microsoft
        // reebok, sony, columbia, audi, hilton, mozilla, BMW
        // taiwan, renault

        val lngCode = "es"

        val awaitable = Observable
            .fromStateAction(searchTweets)(TwitSearchState(twitter, "sony", lngCode))
            .concatMap { Observable.fromIterable(_) } // Seq[Status] => Observable[Status]
            //.filter { _.lang == Some(lngStr) }
            .map { _.getText() }
            .distinct
            //.filter { detectLang(langDetector, _) != lngStr }
            //.filter { hasHashtagOrMention(_) }
            .take(1000)
            // Consumer.complete
            .consumeWith(Consumer.foreach { writeTweetText(_) })
            .runAsync

        Await.result(awaitable, Duration.Inf) // 0 nanos

        assert(true == true)

        log.info("end")
    }
}
