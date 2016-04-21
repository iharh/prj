package parser

import simple.Simple

import com.clarabridge.common.parser.RuleParserBase
import com.clarabridge.common.parser.listeners.SwimlineSuggestionParserListener
import com.clarabridge.common.parser.listeners.SimpleErrorListener

//import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import spock.lang.Specification
import spock.lang.Unroll

//@Slf4j
class SwimlineParserTests extends Specification {
    private static final Logger log = LoggerFactory.getLogger(SwimlineParserTests.class)

    SwimlineSuggestionParserListener listener
    SimpleErrorListener errorListener
    
    def setup() {
        listener = new SwimlineSuggestionParserListener()
        errorListener = new SimpleErrorListener()
        log.info("SwimlineParserTests setup called !!!")
    }
    
    @Unroll
    def "token extraction for suggestion test"() {
        when:
            def result = RuleParserBase.parse(swimline, listener, errorListener)
        then:
            errorListener.getErrors().size() == 0
            listener.getParserResult().getSimpleTokens().size() == simpleSize
            listener.getParserResult().getQuotedTokens().size() == quotedSize
            listener.getParserResult().getWildcardTokens().size() == wildcardSize
            listener.getParserResult().getMasterTokens().size() == mtokensSize
        where:
            swimline << [
                "",
                "sony, playstation, ps3 ps4",
                "sony, \"sony playstation\", ps3 ps4 \"ps \\\" 4\"",
                "sony \"ps \\\" 4\", sony~2 \"playstation 3\"~4 \"psp\"",
                "sony sony~2 sony* son?",
                "sony CITY:\"GOMEL\" ps4 _natural_id:12345",
                "sony, (playstation  (CITY:\"GOMEL\", ps4))",
                "((cancel, cancels, cancells, canceled, cancell, canceling, cancelling, cancelled, cancellation, cancellations, cancelation, cancelations) AND (subscription, subscriptions)), unsubscrib*,",
                "_mtoken:SONY, NOT bad",
                "_LC:[SONY, NIKON], _doc_time:[20150819180000 TO 20150820175959], _PERIOD(y, -1), SOURCE_URL:\"http://twitter.com\", _mtoken:SONY,, TIME_OF_DAY:\"12:00 UTC\", _catRef:[model:\"Website Online Experience\" path:\"Usability\" node:\"Account Modification\"]",
                "word, NOT word1 word2",
                "word, word1 AND word2, word3"
            ]
            simpleSize   << [0, 4, 3, 1, 1, 2, 1, 0, 0, 0, 2, 2]
            quotedSize   << [0, 0, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0]
            wildcardSize << [0, 0, 0, 0, 2, 0, 0, 1, 0, 0, 0, 0]
            mtokensSize	 << [0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0, 0]
    }
}
