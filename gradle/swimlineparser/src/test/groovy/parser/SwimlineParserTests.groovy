package parser

import com.clarabridge.transformer.ld.CrossModel;
import com.clarabridge.transformer.ld.NormLangDetector;
import com.clarabridge.transformer.ld.StringSourceIterator;
import com.clarabridge.transformer.ld.NormCrossModelScorer.Result;
import com.clarabridge.transformer.ld.compiler.Compiler;
import com.clarabridge.transformer.ld.utils.FileNamesCollector;
//import com.clarabridge.transformer.ld.exceptions.ModelCreatorException;
//import com.clarabridge.transformer.ld.exceptions.MathException;

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
    
    CSVFormat inCsvFormat

    NormLangDetector langDetector

    static final double DESIRED_CONFIDENCE_LEVEL = 0.01; // 0.5;

    def setup() {
        errorListener = new SimpleErrorListener()
        
        inCsvFormat = CSVFormat.DEFAULT
            .withIgnoreSurroundingSpaces(true)
            .withIgnoreEmptyLines(false)
            .withSkipHeaderRecord(true)
            //.withHeader("TEXT");

        def modelDirName = '/data/wrk/clb/ld'
        langDetector = getLangDetector(modelDirName);
    }

    def getLangDetector(String modelDirName) { //throws IOException, ModelCreatorException {
        def modelCollector = new FileNamesCollector()

        def modelDir = new File(modelDirName)
        log.debug('Loading language detector models from "{}"', modelDir.getCanonicalPath())

        modelCollector.addFileMask(modelDir, '*.ldm');
        log.debug('Found {} model(s)', modelCollector.getFiles().size());

        def crossModel = Compiler.createModel(modelCollector.getFiles());
        new NormLangDetector(crossModel, DESIRED_CONFIDENCE_LEVEL);
    }

    @Unroll
    def "token extraction for suggestion test"() {
        when:
            def inFileName = inFileDir + '/' + lang + '/' + fileFolder + '/' + fileName + '.csv'
            def csvParser = CSVParser.parse(new File(inFileName), UTF_8, inCsvFormat)

            def dataHeaderIndices = []
            def dataHeaderNames = []

            def nodes = []

            int slTotal = 0
            int slUnknown = 0
            int slWrong = 0

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

                int idx = 0
                for (; idx < r.size(); ++idx) { // parse node
                    if (idx >= levels)
                        break;
                    def n = r.get(idx)
                    if (StringUtils.isNotBlank(n))
                        nodes[idx] = n
                }

                int curIdx = 0
                for (; idx < r.size(); ++idx) { // parse data
                    if (curIdx >= dataHeaderColumns.size())
                        break

                    if (idx != dataHeaderIndices[curIdx])
                        continue

                    def swimline = r.get(idx)
                    def headerName = dataHeaderNames[curIdx]
                    ++curIdx;

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
                    
                    def text = tokens.stream().collect(Collectors.joining(' '))

                    int maxWidthToTest = text.length()
                    def sourceIterator =  new StringSourceIterator(text, maxWidthToTest)
                    def res = langDetector.analyse(sourceIterator) // throws ModelCreatorException, MathException
                    ++slTotal

                    def node = nodes.stream().collect(Collectors.joining(' -> '))
                    if (res.getConfidenceLevel() <= DESIRED_CONFIDENCE_LEVEL) {
                        log.debug('{} : {}[{}] : {}', 'un', node, headerName, text)
                        ++slUnknown
                        continue
                    }

                    def detectedCode = res.getLangCode()
                    if (detectedCode == lang)
                        continue
                    
                    ++slWrong
                    log.debug('{} : {}[{}] : {}', detectedCode, node, headerName, text)
                }
            }
            log.info('{}, {}, {}, {}, {}', fileName, lang, slTotal, slUnknown, slWrong)

        then:
            errorListener.getErrors().size() >= 0
            //listener.getParserResult().getWildcardTokens()
        where:
            lang | fileFolder             | fileName               | levels
            'en' | 'high_tech_computing'  | 'High_Tech_Computing'  | 3
            'en' | 'hr_employee_feedback' | 'HR_Employee_Feedback' | 2
            'en' | 'insurance_model'      | 'Insurance_Model'      | 3
            'en' | 'lodging'              | 'Lodging'              | 4
    }

    def unquote(String s) {
        (s.startsWith('"') && s.endsWith('"')) ? s.substring(1, s.length() - 1) : s
    }
}
