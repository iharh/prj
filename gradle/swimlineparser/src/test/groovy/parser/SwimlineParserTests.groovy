package parser

import simple.Simple

import com.clarabridge.common.parser.RuleParserBase
import com.clarabridge.common.parser.listeners.SwimlineSuggestionParserListener
import com.clarabridge.common.parser.listeners.SimpleErrorListener

import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
//import org.apache.commons.csv.CSVRecord

import org.apache.commons.lang3.StringUtils

import org.slf4j.Logger
import org.slf4j.LoggerFactory
//import lombok.extern.slf4j.Slf4j

import java.util.stream.Collectors

import spock.lang.Specification
import spock.lang.Unroll

import static java.nio.charset.StandardCharsets.*;

//@Slf4j
class SwimlineParserTests extends Specification {
    static final Logger log = LoggerFactory.getLogger(SwimlineParserTests.class)

    static final inFileDir = '/data/wrk/industry-templates/categorization'

    static final dataHeaderColumns = ["Keywords", "And Words", "And(2) Words", "Not Words", "Verbatim Keywords", "Verbatim And Words", "Verbatim And(2) Words", "Verbatim Not Words"]

    SimpleErrorListener errorListener
    
    CSVFormat csvFormat

    def setup() {
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
            def inFileName = inFileDir + '/en/lodging/Lodging.csv'
            def csvParser = CSVParser.parse(new File(inFileName), UTF_8, csvFormat)

            def dataHeaderIndices = []
            def dataHeaderNames = []


            for (r in csvParser) {
                if (r.size() < dataHeaderColumns.size()) // skip info
                    continue;

                if (dataHeaderIndices.isEmpty()) { // detect headers
                    for (int idx = 0; idx < r.size(); ++idx) {
                        def s = r.get(idx)
                        if (dataHeaderColumns.indexOf(s) >= 0) {
                            dataHeaderIndices.add(idx)
                            dataHeaderNames.add(s)
                        }
                    }
                    //for (idx in dataHeaderIndices) log.info('idx: {}', idx)
                    //for (n in dataHeaderNames) log.info('n: {}', n)
                    continue
                }

                int curIdx = 0
                for (int idx = 0; idx < r.size(); ++idx) { // parse data
                    if (curIdx >= dataHeaderColumns.size())
                        break

                    if (idx != dataHeaderIndices[curIdx])
                        continue

                    def swimline = r.get(idx)
                    if (StringUtils.isBlank(swimline))
                        continue

                    def listener = new SwimlineSuggestionParserListener()
                    def result = RuleParserBase.parse(swimline, listener, errorListener)

                    def tokens = []
                    def parserResult = listener.getParserResult()
                    tokens.addAll(parserResult.getSimpleTokens())
                    tokens.addAll(parserResult.getMasterTokens())

		    for (quotedToken in parserResult.getQuotedTokens())
			tokens.add(unquote(quotedToken));

                    if (tokens.isEmpty())
                        continue
                    
                    def tokensStr = tokens.stream().collect(Collectors.joining(" "))

                    log.info('{} : {}', dataHeaderNames[curIdx], tokensStr)

                    ++curIdx;
                }
            }

        then:
            errorListener.getErrors().size() == 0
            //listener.getParserResult().getQuotedTokens()
            //listener.getParserResult().getWildcardTokens()
    }

    def unquote(String s) {
        (s.startsWith('"') && s.endsWith('"')) ? s.substring(1, s.length() - 1) : s
    }
}
