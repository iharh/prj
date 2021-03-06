package mpkg;

import mdm.parser.QueryParser;
import mdm.parser.QueryParserResult;
import mdm.parser.LuceneSearcher;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.regex.MatchResult;

import java.util.List;
import java.util.LinkedList;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

public class QueryParserTest {
    private static String dumpAllGroups(Pattern p, String q) {
        StringBuilder result = new StringBuilder();

        String query = QueryParser.omitNegated(q);

        Matcher matcher = p.matcher(query);
        while (matcher.find()) {
            MatchResult mr = matcher.toMatchResult();
            int gcnt = mr.groupCount();
            result.append("\n-- matchResult ").append(mr.start()).append("-").append(mr.end()).append(" gcnt ").append(gcnt);
            for (int i = 0; i < gcnt; ++i) {
                result.append("\n\tgroup(").append(i).append(") ").append(mr.start(i)).append("-").append(mr.end(i)).append(" : ").append(mr.group(i));
            }
        }
        return result.toString();
    }

    //private static final String patSP = "(?:[\\s]*)";
    private static final String patSP = "[\\s]*"; // \\s*

    @Test
    public void testRegr1() {
        final String q = "_catRef:[model:\"v1\"]"
                       + ", "
                       + "_catRef:[model:\"v2\"]";

        //String p = "(\\s*[\\w]+\\s*)";
        //final String q = "aaa1    bbb1 ccc1";
        //assertEquals("", dumpAllGroups(QueryParser.getPattern(), q));

        final String wPat1 = "\\w";
        final String wPatCaret = "\\^";
        String PATTERN_SIMPLE_WORD = "([\\s]*" + wPat1 + "\\d[" + wPatCaret + "\\_\\&\\@\\#\\-\\/\\'\\~\\.\\$%\\u20ac???*?]+[\\s]*)"; //$NON-NLS-1$

        final String patCatRef = mdm.parser.CatRefParserImpl.NR_REGEXP; /// "_catRef\\s*:\\s*\\[((model:)\\s*\"([^\"]+)\")?\\s*\\]";

        String full_p = "\\s*(" + patCatRef + ")|" + PATTERN_SIMPLE_WORD;

        //assertEquals("", dumpAllGroups(Pattern.compile(full_p/*, Pattern.UNICODE_CHARACTER_CLASS*/), q));
        assertTrue(true);
    }

    @Test
    public void testPipeSeparator() {
        final String q = "_catRef:[" +
            "model:\"v1Products Model - Tuning Base Model\"" +
            " path:\"v1Enterprise|Poweredge Modular Servers\"" +
            " node:\"v1Mellanox IB m2401g\"" +
            "]" +
            ", _catRef:[" +
            "model:\"v2Products Model - Tuning Base Model\"" +
            " path:\"v2Enterprise|Poweredge Modular Servers\"" +
            " node:\"v2Power Edge m2401g\"" +
            "]";

        QueryParserResult result = QueryParser.parse(q);
        assertEquals(0, result.getQuotedTerms().size());
        assertEquals(0, result.getLcPairs().size());
        assertEquals(0, result.getTerms().size()); // need 0
        assertEquals(0, result.getAttributeTerms().size()); // need 0
    }

    @Test
    public void testParseSmiley() {
        final String q = "\"<:)>\"";
        QueryParserResult result = QueryParser.parse(q);
        assertEquals(0, result.getLcPairs().size());
        assertEquals(0, result.getTerms().size());
        assertThat(result.getQuotedTerms(), containsInAnyOrder(q));
    }

    @Test
    public void testParseFr() {
        QueryParserResult result = QueryParser.parse("_lc:(acheter\\ -->\\ fleur_?)");
        assertEquals(0, result.getQuotedTerms().size());
        assertEquals(0, result.getTerms().size());
        assertEquals(1, result.getLcPairs().size());
    }

    @Test
    public void testParseAr() {
        QueryParserResult result = QueryParser.parse("_lc:(??????????????????\\ -->\\ ????????_?)");
        assertEquals(0, result.getQuotedTerms().size());
        assertEquals(0, result.getTerms().size());
        assertEquals(1, result.getLcPairs().size());
    }

    @Test
    public void testParse() {
        String testQuery = " business_date: [3246883 to kjdfhsdjf]  " +
        		"(Cuban AND Crisis ) \" AND ()asjd \"  Location: \"New York\"  CUSTOMER: Apple   &&";
        QueryParserResult result = QueryParser.parse(testQuery);
        assertEquals(2, result.getAttributeTerms().size());
        assertEquals(1, result.getRangeAttributeTerms().size());
        assertEquals(1, result.getQuotedTerms().size());
        assertEquals(2, result.getSyntaxTerms().size());
        assertEquals(2, result.getTerms().size());
        assertEquals(0, result.getLcPairs().size());
    }
    
    @Test
    public void testParse1() {
        String testQuery = "Wo*rd1, wprds2?, \"wasdh^&^or*d3\",Location: \"New York\",CUSTOMER: Apple";
        QueryParserResult result = QueryParser.parse(testQuery);

        assertThat(result.getTerms(), containsInAnyOrder("Wo*rd1", " wprds2?"));
        assertThat(result.getQuotedTerms(), containsInAnyOrder(" \"wasdh^&^or*d3\""));

        assertEquals(2, result.getAttributeTerms().size());
        assertEquals(0, result.getSyntaxTerms().size());
        assertEquals(0, result.getLcPairs().size());
    }
    
    @Test
    public void testParse2() {
        String testQuery = "business_date: [3246883 to kjdfhsdjf] doc_date: [3246883 23432434]";
        QueryParserResult result = QueryParser.parse(testQuery);

        assertEquals(result.getAttributeTerms().size(), 0);
        assertEquals(result.getRangeAttributeTerms().size(), 2);
        assertEquals(result.getQuotedTerms().size(), 0);
        assertEquals(result.getSyntaxTerms().size(), 0);
        assertEquals(result.getTerms().size(), 0);
        assertEquals(result.getLcPairs().size(), 0);
    }
    
    @Test
    public void testParse3() {
        String testQuery = "&& || OR AND and or | |";
        QueryParserResult result = QueryParser.parse(testQuery);

        assertEquals(result.getAttributeTerms().size(), 0);
        assertEquals(result.getQuotedTerms().size(), 0);
        assertEquals(result.getSyntaxTerms().size(), 4);
        assertEquals(result.getTerms().size(), 2);
        assertEquals(result.getLcPairs().size(), 0);
    }
    
    @Test
    public void testParse4() {
        String testQuery = "\"askldl\" skjdklsdk\"";
        QueryParserResult result = QueryParser.parse(testQuery);

        assertEquals(0, result.getAttributeTerms().size());
        assertEquals(1, result.getQuotedTerms().size());
        assertEquals(0, result.getSyntaxTerms().size());
        assertEquals(1, result.getTerms().size());
        assertEquals(0, result.getLcPairs().size());
    }
    
    @Test
    public void testParse5() {
        String testQuery = "business_date: [3246883 to kjdfhsdjf] doc_date: [3246883 23432434] _lc:(hello\\ -->\\ word_1) _lc:(good*\\ -->\\ xxx_?) \"hello word\"";
        QueryParserResult result = QueryParser.parse(testQuery);

        assertEquals(0, result.getAttributeTerms().size());
        assertEquals(2, result.getRangeAttributeTerms().size());
        assertEquals(1, result.getQuotedTerms().size());
        assertEquals(0, result.getSyntaxTerms().size());
        assertEquals(0, result.getTerms().size());
        assertEquals(2, result.getLcPairs().size());
    }

    @Test
    public void testParseNumeric() {
        String testQuery = "a:1, b:2";
        QueryParserResult result = QueryParser.parse(testQuery);

        assertEquals(2, result.getAttributeTerms().size());
        assertEquals(0, result.getRangeAttributeTerms().size());
        assertEquals(0, result.getQuotedTerms().size());
        assertEquals(0, result.getSyntaxTerms().size());
        assertEquals(0, result.getTerms().size());
        assertEquals(0, result.getLcPairs().size());
    }

    @Test
    public void testParseUndefined() {
        String testQuery = "( ((ADDRESS:\"undefined\") OR (NOT(ADDRESS:*))))";
        String result = LuceneSearcher.wrap(testQuery);        
        assertEquals("( (( address:\"undefined\" ) OR (NOT( address:* ))))", result);
    }

    @Test
    public void testParseSimilarNames() {
        String testQuery = "HOTEL_CITY:londona, HOTEL_CITY:london, HOTEL_CITY:london2,"
        		+ " HOTEL_CITY:\"london'2\", FIRSTNAME:\"d'marie\"";
        String result = LuceneSearcher.wrap(testQuery);
        assertEquals(result, " hotel_city:londona , hotel_city:london , hotel_city:london2 ,"
        		+ " hotel_city:\"london'2\" , firstname:\"d'marie\" ");
    }
  
    @Test
    public void testParseAttrWildcardedURL() {
        String testQuery = "REF_URL:http\\://mail.aol*, REF_URL:http\\://mail.aol?, REF_URL:\"http://mail.aol\"";
        String result = LuceneSearcher.wrap(testQuery);
        assertEquals(result, " ref_url:http\\://mail.aol* , ref_url:http\\://mail.aol? , ref_url:\"http\\://mail.aol\" " );
    }
   
    @Test
    public void testParseAttrEscapedChars() {
        String testQuery = "ATTR:\\+\\-\\!\\(\\)\\{\\}\\[\\]\\^\\\"\\~\\*\\?\\:\\\\";
        String result = LuceneSearcher.wrap(testQuery);
        assertEquals(result, " attr:\\+\\-\\!\\(\\)\\{\\}\\[\\]\\^\\\"\\~\\*\\?\\:\\\\ ");
    }
   
    @Test
    public void testParseNoNeedForEscapingInsideQuotesExceptDoubleQuote() {
        String testQuery = "ATTR:\"+-!(){}[]^\\\"~*?\\ \"";
        String result = LuceneSearcher.wrap(testQuery);
        assertEquals(result, " attr:\"+-!(){}[]^\\\"~*?\\\\ \" ");
    }    
   
    @Test
    public void testParseAttrWithLastQuote() {
        String testQuery = "ATTR:\"abc\\\"def\"";
        String result = LuceneSearcher.wrap(testQuery);
        assertEquals(result, " attr:\"abc\\\"def\" ");
    }
   
    @Test
    public void testReplaceExactWord_LastWord() {
        final String source = " _id_source:deepti1 , _id_source:deepti";
        final String attribute = " _id_source:deepti";
        final String newAttr = "_id_source:deepti";
        assertEquals(" _id_source:deepti1 , _id_source:deepti ",
                       QueryParser.replaceFirstAttribute(source, attribute, " " + newAttr + " "));
    }
};
