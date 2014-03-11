package mpkg;

import mdm.parser.QueryParser;
import mdm.parser.QueryParserResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.List;
import java.util.LinkedList;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsCollectionContaining.hasItems;
import static org.hamcrest.collection.IsEmptyCollection.empty;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;

public class QPCustomTest {
    private static Pattern getPattern() {
        final String wPat = "\\w\\^\\<\\:\\>"; // \\p{javaLetterOrDigit}";
        //final String wPat = "\\w";
        final String wPatNQ = "&&[^\"]";

        final String PATTERN_SIMPLE_WORD = 
            "([\\s]*[" + wPat + "\\_\\&\\@\\#\\-\\/\\'\\~\\.\\d\\$%\\u20ac�*?]+[\\s]*)";
        final String PATTERN_QUOTED_WORD = 
            "([\\s]*\"{1}[\\s\\d" + wPat + "\\$%\\u20ac�\\p{Punct}" + wPatNQ + "]+\"{1}[\\s]*)";

        return Pattern.compile(
            PATTERN_SIMPLE_WORD + "|" + PATTERN_QUOTED_WORD, Pattern.UNICODE_CHARACTER_CLASS
        );
    }

    private static void getTerms(final String query, List<String> terms, List<String> quotedTerms) {
        terms.clear();
        quotedTerms.clear();
        final Matcher matcher = getPattern().matcher(query); 

        while (matcher.find()) {
            final String w = matcher.group(0);
            if (matcher.group(1) != null) {
                terms.add(w);
            } else if (matcher.group(2) != null) {
                quotedTerms.add(w);
            }
        }
    }

    private static final String q1 = "Wo*rd1, wprds2?, \"wasdh^&^or*d3\""; // ",Location: \"New York\",CUSTOMER: Apple";

    @Ignore
    public void testRegexUnicode() throws Exception {
        List<String> terms = new LinkedList<String>();
        List<String> quotedTerms = new LinkedList<String>();

        getTerms(q1, terms, quotedTerms);
        assertThat(terms, containsInAnyOrder("Wo*rd1", " wprds2?"));
        assertThat(quotedTerms, hasItem(" \"wasdh^&^or*d3\""));

        getTerms("طَبِيعِيّ, جِدّ, \"a^bc\"", terms, quotedTerms);
        assertThat(terms, containsInAnyOrder("طَبِيعِيّ", " جِدّ"));
        assertThat(quotedTerms, containsInAnyOrder(" \"a^bc\""));

        getTerms("\"<:)>\"", terms, quotedTerms);
        assertThat(terms, empty());
        assertThat(quotedTerms, hasItem("\"<:)>\""));

        //assertEquals("\\w", Pattern.compile("\\w", Pattern.UNICODE_CHARACTER_CLASS).toString());
    }
};
