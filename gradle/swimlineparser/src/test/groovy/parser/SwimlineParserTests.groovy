package parser

import simple.Simple

import com.clarabridge.common.parser.RuleParserBase
import com.clarabridge.common.parser.listeners.SwimlineSuggestionParserListener
import com.clarabridge.common.parser.listeners.SimpleErrorListener

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

//import lombok.extern.slf4j.Slf4j
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import spock.lang.Specification
import spock.lang.Unroll

import static java.nio.charset.StandardCharsets.*;

//@Slf4j
class SwimlineParserTests extends Specification {
    private static final Logger log = LoggerFactory.getLogger(SwimlineParserTests.class)

    SwimlineSuggestionParserListener listener
    SimpleErrorListener errorListener
    
    CSVFormat csvFormat

    def setup() {
        listener = new SwimlineSuggestionParserListener()
        errorListener = new SimpleErrorListener()
        
        csvFormat = CSVFormat.DEFAULT
            .withIgnoreSurroundingSpaces(true)
            .withIgnoreEmptyLines(false)
            .withSkipHeaderRecord(true)
            //.withHeader("TEXT");

    }
    
    @Unroll
    def "token extraction for suggestion test"() {
        when:
            //def result = RuleParserBase.parse(swimline, listener, errorListener)

            def inFileName = '/data/wrk/industry-templates/categorization/en/lodging/Lodging.csv'
            def csvParser = CSVParser.parse(new File(inFileName), UTF_8, csvFormat);
            for (final CSVRecord r : csvParser) {
                final String text = r.get(0);
                log.info("text: {}", text)
            }

        then:
            errorListener.getErrors().size() == 0
            //listener.getParserResult().getSimpleTokens()
            //listener.getParserResult().getQuotedTokens()
            //listener.getParserResult().getWildcardTokens()
            //listener.getParserResult().getMasterTokens()
    }
}
