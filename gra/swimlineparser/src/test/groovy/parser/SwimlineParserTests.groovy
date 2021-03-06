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
//import spock.lang.Shared

import static java.nio.charset.StandardCharsets.*;

//@Slf4j
class SwimlineParserTests extends Specification {
    static final Logger log = LoggerFactory.getLogger(SwimlineParserTests.class)

    static final inFileDir = '/data/wrk/industry-templates/categorization'

    static final dataHeaderColumns = ["Keywords", "And Words", "And(2) Words", "Not Words", "Verbatim Keywords", "Verbatim And Words", "Verbatim And(2) Words", "Verbatim Not Words"]

    
    CSVFormat inCsvFormat

    NormLangDetector langDetector
    //SimpleErrorListener errorListener

    static final double DESIRED_CONFIDENCE_LEVEL = 0.01; // 0.5;

    def setupSpec() {
        log.info('{}, {}, {}, {}, {}', 'MODEL', 'LANG', 'TOTAL', 'UN', 'WRONG')
    }

    def setup() {
        //errorListener = new SimpleErrorListener()

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
                    def errorListener = new SimpleErrorListener()
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

                    errorListener.getErrors().each {
                        log.info('parse error: {}', it)
                    }
                }
            }
            log.info('{}, {}, {}, {}, {}', fileName, lang, slTotal, slUnknown, slWrong)

        then:
            1 == 1
            // errorListener.getErrors().size() == 0
            // listener.getParserResult().getWildcardTokens()
        where:
            lang | fileFolder                            | fileName                              | levels
            'de' | 'basic_emotions'                      | 'Basic_Emotions'                      | 3
            'de' | 'high_tech_computing'                 | 'High_Tech_Computing'                 | 3
            'de' | 'lodging'                             | 'Lodging'                             | 3
            'de' | 'restaurants'                         | 'Restaurants'                         | 3
            'de' | 'retail_online'                       | 'Retail_Online'                       | 3
            'de' | 'retail_store'                        | 'Retail_Store'                        | 3
            //
            'en' | 'automotive'                          | 'Automotive'                          | 3
            'en' | 'banking_institutional'               | 'Banking_Institutional'               | 3
            'en' | 'banking_retail'                      | 'Banking_Retail'                      | 3
            'en' | 'basic_emotions'                      | 'Basic_Emotions'                      | 3
            'en' | 'categorization_library'              | 'Categorization_Library'              | 4
            'en' | 'customer_escalation'                 | 'Customer_Escalation'                 | 3
            'en' | 'customer_profiles'                   | 'Customer_Profiles'                   | 4
            'en' | 'customer_support'                    | 'Customer_Support'                    | 3
            'en' | 'engage_model'                        | 'Engage_Model'                        | 3
            'en' | 'food_beverage_experience_model'      | 'Food_Beverage_Experience_Model'      | 3
            'en' | 'high_tech_computing'                 | 'High_Tech_Computing'                 | 3
            'en' | 'hr_employee_feedback'                | 'HR_Employee_Feedback'                | 2
            'en' | 'insurance_model'                     | 'Insurance_Model'                     | 3
            'en' | 'lodging'                             | 'Lodging'                             | 4
            'en' | 'loyalty_programs'                    | 'Loyalty_Programs'                    | 3
            'en' | 'marketing_and_advertising'           | 'Marketing_and_Advertising'           | 3
            'en' | 'restaurants'                         | 'Restaurants'                         | 3
            'en' | 'retail_online'                       | 'Retail_Online'                       | 3
            'en' | 'retail_product_evaluation'           | 'Retail_Product_Evaluation'           | 3
            'en' | 'retail_store'                        | 'Retail_Store'                        | 3
            'en' | 'social_media_categorization_library' | 'Social_Media_Categorization_Library' | 3
            'en' | 'suggestions'                         | 'Suggestions'                         | 3
            'en' | 'telecom_mobile'                      | 'Telecom_Mobile'                      | 3
            'en' | 'transportation'                      | 'Transportation'                      | 3
            'en' | 'website_online_experience'           | 'Website_Online_Experience'           | 3
            //
            'es' | 'automotive'                          | 'Automotive'                          | 3
            'es' | 'banking_institutional'               | 'Banking_Institutional'               | 3
            'es' | 'banking_retail'                      | 'Banking_Retail'                      | 3
            'es' | 'basic_emotions'                      | 'Basic_Emotions'                      | 3
            'es' | 'categorization_library'              | 'Categorization_Library'              | 2
            'es' | 'customer_escalation'                 | 'Customer_Escalation'                 | 3
            'es' | 'customer_profiles'                   | 'Customer_Profiles'                   | 4
            'es' | 'customer_support'                    | 'Customer_Support'                    | 3
            'es' | 'high_tech_computing'                 | 'High_Tech_Computing'                 | 3
            'es' | 'hr_employee_feedback'                | 'HR_Employee_Feedback'                | 2
            'es' | 'lodging'                             | 'Lodging'                             | 3
            'es' | 'marketing_and_advertising'           | 'Marketing_and_Advertising'           | 3
            'es' | 'restaurants'                         | 'Restaurants'                         | 3
            'es' | 'retail_online'                       | 'Retail_Online'                       | 3
            'es' | 'retail_store'                        | 'Retail_Store'                        | 3
            'es' | 'social_media_categorization_library' | 'Social_Media_Categorization_Library' | 3
            'es' | 'suggestions'                         | 'Suggestions'                         | 3
            'es' | 'telecom_mobile'                      | 'Telecom_Mobile'                      | 3
            'es' | 'transportation'                      | 'Transportation'                      | 3
            'en' | 'website_online_experience'           | 'Website_Online_Experience'           | 3
            //
            'fr' | 'basic_emotions'                      | 'Basic_Emotions'                      | 3
            'fr' | 'high_tech_computing'                 | 'High_Tech_Computing'                 | 3
            'fr' | 'lodging'                             | 'Lodging'                             | 3
            'fr' | 'restaurants'                         | 'Restaurants'                         | 3
            'fr' | 'retail_online'                       | 'Retail_Online'                       | 3
            'fr' | 'retail_store'                        | 'Retail_Store'                        | 3
            //
            'it' | 'basic_emotions'                      | 'Basic_Emotions'                      | 3
            'it' | 'high_tech_computing'                 | 'High_Tech_Computing'                 | 3
            'it' | 'lodging'                             | 'Lodging'                             | 3
            'it' | 'restaurants'                         | 'Restaurants'                         | 3
            'it' | 'retail_online'                       | 'Retail_Online'                       | 3
            'it' | 'retail_store'                        | 'Retail_Store'                        | 3
            //
            'ja' | 'basic_emotions'                      | 'Basic_Emotions'                      | 3
            'ja' | 'high_tech_computing'                 | 'High_Tech_Computing'                 | 3
            'ja' | 'lodging'                             | 'Lodging'                             | 3
            'ja' | 'restaurants'                         | 'Restaurants'                         | 3
            'ja' | 'retail_online'                       | 'Retail_Online'                       | 3
            'ja' | 'retail_store'                        | 'Retail_Store'                        | 3
            //
            'nl' | 'basic_emotions'                      | 'Basic_Emotions'                      | 3
            'nl' | 'high_tech_computing'                 | 'High_Tech_Computing'                 | 3
            'nl' | 'lodging'                             | 'Lodging'                             | 3
            'nl' | 'restaurants'                         | 'Restaurants'                         | 3
            'nl' | 'retail_online'                       | 'Retail_Online'                       | 3
            'nl' | 'retail_store'                        | 'Retail_Store'                        | 3
            //
            'pt' | 'basic_emotions'                      | 'Basic_Emotions'                      | 3
            'pt' | 'high_tech_computing'                 | 'High_Tech_Computing'                 | 3
            'pt' | 'lodging'                             | 'Lodging'                             | 3
            'pt' | 'restaurants'                         | 'Restaurants'                         | 3
            'pt' | 'retail_online'                       | 'Retail_Online'                       | 3
            'pt' | 'retail_store'                        | 'Retail_Store'                        | 3
            //
            'zh' | 'basic_emotions'                      | 'Basic_Emotions'                      | 3
            'zh' | 'high_tech_computing'                 | 'High_Tech_Computing'                 | 3
            'zh' | 'lodging'                             | 'Lodging'                             | 3
            'zh' | 'restaurants'                         | 'Restaurants'                         | 3
            'zh' | 'retail_online'                       | 'Retail_Online'                       | 3
            'zh' | 'retail_store'                        | 'Retail_Store'                        | 3
    }

    def unquote(String s) {
        (s.startsWith('"') && s.endsWith('"')) ? s.substring(1, s.length() - 1) : s
    }
}
