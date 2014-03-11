package mdm.parser;

import java.util.regex.Matcher;

import java.util.regex.Matcher;

public final class LuceneSearcher {
    private static final String FIELD_NAME_DOC_TIME = "_doc_time";

    public static String wrap(String word) {
        String newWord = word;
        if (QueryParser.isAttribute(word)) {
        QueryParserResult result = QueryParser.parse(word);
        for (String attribute : result.getAttributeTerms()) {
            if (!attribute.startsWith(/*LuceneConstants.*/FIELD_NAME_DOC_TIME)) {
                String newAttr = attribute.trim();
                String value = QueryParser.extractAttributeValue(newAttr);
                // Don't escape if escaped already
                newAttr = newAttr.replace(value, value.replaceAll("(?<!\\\\):", "\\\\:"));
                newAttr = newAttr.replaceAll(" ", "\\\\ ");
                // we can't use String.replace or String.replaceFirst (see javadocs why) because we
                // need to replace only first occurrence of specified attribute with plain(!) text, not regex
                // for such purposes we get String.replace method and changing it replace first occurrence only, not all
                newWord = QueryParser.replaceFirstAttribute(newWord, attribute, " " + newAttr.toLowerCase() + " ");
            }
        }
        } else {
            // Replacing ONE slash (represented as "\\" regexp and "\\\\" Java string with two slashes
            newWord = newWord.replaceAll("\\\\", Matcher.quoteReplacement("\\\\"));
        }
        return newWord;
    } 
}

