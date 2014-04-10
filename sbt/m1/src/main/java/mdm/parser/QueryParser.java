package mdm.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class QueryParser {
	private static final Logger LOG = LoggerFactory.getLogger(QueryParser.class);

	public static final String PATTERN_ATTRIBUTE = 
        // Attribute name, followed by colon.
        "(([\\s]*[\\p{L}\\p{N}\\p{Pc}]+[\\s]*){1}:{1}(" 
            // Quoted value - sent to Lucene QueryParser as a phrase term.
            + "([\\s]*\"(?:\\\\\"|[^\"])+\"[\\s]*)|"
            // Any text with escaped Lucene special characters, space and comma.
            // Text should not start with '(' and '['.
            + "([\\s]*(?:[^\\s,\\\\()\\[]|\\\\[\\s,+\\-!(){}\\[\\]\\^\"~*?:\\\\])+[\\s]*)|"
            // Ranged value, example: [3246883 to kjdfhsdjf].
            + "([\\s]*\\[[\\s[\\p{L}\\p{N}\\p{Pc}]\\p{Punct}&&[^\\]]]+\\][\\s]*)" 
            + "){1})";

	
	public static final Pattern PATTERN_ATTRIBUTE_PATTERN = Pattern.compile(PATTERN_ATTRIBUTE);

        private static final String wPat1 = "\\w"; // \\p{javaLetterOrDigit}"; //$NON-NLS-1$
        private static final String wPatCaret = "\\^"; //$NON-NLS-1$
        private static final String wPat2 = wPat1 + wPatCaret;
        private static final String wPat3 = wPat2 + "\\<\\:\\>"; //$NON-NLS-1$
        private static final String wPatNQ = "&&[^\"]"; //$NON-NLS-1$

        private static final String PATTERN_SIMPLE_WORD =
            "([\\s]*[" + wPat2 + "\\_\\&\\@\\#\\-\\/\\'\\~\\.\\d\\$%\\u20ac�*?]+[\\s]*)"; //$NON-NLS-1$

	private static final String PATTERN_QUOTED_WORD = 
	    "([\\s]*\"{1}[" + wPat3 + "\\s\\d\\$%\\u20ac�\\p{Punct}" + wPatNQ + "]+\"{1}[\\s]*)"; //$NON-NLS-1$

	private static final String PATTERN_SYNTSX_WORD = 
	    "([\\s]*AND+[\\s]*)|([\\s]*OR+[\\s]*)|"
			+ "([\\s]*\\&{2}[\\s]*)|([\\s]*\\|{2}[\\s]*)"; //$NON-NLS-1$ 
	//private static final String PATTERN_SYNTSX_WORD_FIXED = 
        //       "(\\s*AND+\\s*)"
        //    + "|(\\s*OR+\\s*)"
        //    + "|(\\s*\\&{2}\\s*)"
        //    + "|(\\s*\\|{2}\\s*)"; //$NON-NLS-1$ 

	private static final String LC_RULE = 
	    "(\\s*_lc:\\(?\\s*([" + wPat1 + "\\*\\?\\s\\\\]+)\\\\ " //$NON-NLS-1$
			+ "-->\\\\ ([" + wPat1 + "\\*\\?\\s\\\\]+)_([0,1,\\?]+)\\s*\\)?\\s*)"; //$NON-NLS-1$

	private static Pattern pattern;
	
	private static Pattern negPattern;

	private QueryParser() {
		super();
	}

	public static QueryParserResult parse(String origQuery) {
		// CSI-1352: Prevent highlighting negated words in queries like: (+_words:wash -(+_words:my +_words:pants))
		String query = omitNegated(origQuery);
		
		Matcher matcher = getPattern().matcher(query);
		QueryParserResult result = new QueryParserResult();
		while (matcher.find()) {

			String matchedTerm = matcher.group(0);
			if (matcher.group(13) != null && matcher.group(14) != null && matcher.group(15) != null) {
				result.addLcPair(new LcPair(matcher.group(13), matcher.group(14),	matcher.group(15)));
			} else if (matcher.group(21) != null) {
				result.addRangeAttributeTerm(matchedTerm);
			} else 	if ((matcher.group(17) != null) 
			                && (!matchedTerm.toLowerCase().contains("_lc"))) {
				if (matcher.group(17).equals("_words")) {
					int pos = matcher.start(17);
					if (pos == 0 || query.charAt(pos - 1) != '-') { // not for negated words
					result.addTerm(matcher.group(18).trim());
					}
				} else {
					result.addAttributeTerm(matchedTerm);
				}
			} else if (matcher.group(26) != null) {
				result.addTerm(matchedTerm);
			} else if (matcher.group(27) != null) {
				result.addQuotedTerm(matchedTerm);
			} else if ((matcher.group(22) != null)
					|| (matcher.group(23) != null)
					|| (matcher.group(24) != null)
					|| (matcher.group(25) != null)) {
				result.addSyntaxTerm(matchedTerm);
			} 
		}

		return result;
	}

    public static String replaceFirstAttribute(final String source, final String oldValue,
                    final String newValue) {
        final Matcher matcher = getPattern().matcher(source);
        while (matcher.find()) {
            final String matchedTerm = matcher.group(0);
            final boolean isLcPair =
                            matcher.group(13) != null && matcher.group(14) != null
                                            && matcher.group(15) != null;
            final boolean rangeAttribute = !isLcPair && matcher.group(21) != null;
            if (!rangeAttribute && matcher.group(17) != null
                            && !matchedTerm.toLowerCase().contains("_lc")) {
                if (!matcher.group(17).equals("_words") && matchedTerm.equals(oldValue)) {
                    final int startIndex = matcher.start();
                    final StringBuilder buffer = new StringBuilder(source);
                    buffer.delete(startIndex, startIndex + oldValue.length());
                    buffer.insert(startIndex, newValue.toLowerCase());
                    return buffer.toString();
                }
            }
        }
        return source;
    }

	public static String replaceFirstTerm(final String source, final String oldValue, final String newValue) {
		
		final Matcher matcher = getPattern().matcher(source);
		while (matcher.find()) {
			final String matchedTerm = matcher.group(0);
			if ((matcher.group(26) != null || matcher.group(27) != null) && matchedTerm.equals(oldValue)) {
				final int startIndex = matcher.start();
				final StringBuilder buffer = new StringBuilder(source);
				// do not replace fuzzy token
				if (!matchedTerm.startsWith("~")) {
					buffer.delete(startIndex, startIndex + oldValue.length());
					buffer.insert(startIndex, newValue);
				}
				return buffer.toString();
			}
		}
		return source;
	}    
    
	/*
	 * CSI-1352: Prevent highlighting negated words in queries like: (+_words:wash -(+_words:my +_words:pants))
	 * TODO: this is quick crutch, it does not cover "-(" in attributes, 
	 *       the best solution is utilizing tools like at JFlex at top level of rule parsing.
	 */
	public static String omitNegated(String s) {
		if (negPattern == null) {
			negPattern = Pattern.compile("-\\s*(\\()"); 
		}
		Matcher m = negPattern.matcher(s);
		
		StringBuilder result = new StringBuilder();
		int curPos = 0;
		
		while (m.find(curPos)) {
			String prevString = s.substring(curPos, m.start());
			result.append(prevString);
			curPos = getBalancedBracketEndPos(s, m.start(1)); // Position of opening bracket.
		}
		
		result.append(s.substring(curPos));
		return result.toString();  
	}
	
	/**
	 * @return index of the balanced closing bracket in the string. 
	 * Takes in account escaping and quotes.
	 */
	private static int getBalancedBracketEndPos(String s, int pos) {
	    int nesting = 0;
	    boolean quoted = false;
	    boolean escaped = true;
	    char c = 0;
	    char prevC = 0;
	    
	    for (int i = pos, n = s.length(); i < n; ++i) {
	    	prevC = c;
	        c = s.charAt(i);
	        
	        if (c == '\\') {
	        	escaped = prevC == c ? false : true;
	        } else {
	        	escaped = false;
	        }
	        if (escaped) continue;
	        
	        if (c == '"' && !escaped) {
	        	quoted = !quoted;
	        	if (quoted) continue;
	        }
	        
	        switch (c) {
	            case '(':
	                nesting++;
	                break;
	            case ')':
	                nesting--;
	                if (nesting == 0) {
	                	return ++i;
	                }
	                break;
	        }
	        
	    }
	        
        if (nesting != 0) {
        	LOG.warn("Illegal nesting detected in query: {}", s);
        }
        
        return s.length();
	}

	public static boolean isAttribute(String word) {
		return PATTERN_ATTRIBUTE_PATTERN.matcher(word).find();
	}
	

    public static String extractAttributeValue(String word) {
        Matcher matcher = PATTERN_ATTRIBUTE_PATTERN.matcher(word);
        if (matcher.find() && matcher.groupCount() > 3) {
            return matcher.group(3);
        } else {
            return "";
        }
    }
    
    public static String extractAttributeName(String word) {
        Matcher matcher = PATTERN_ATTRIBUTE_PATTERN.matcher(word);
        if (matcher.find() && matcher.groupCount() > 2) {
            return matcher.group(2);
        } else {
            return "";
        }
    }

    private static final String pSPC = "\\s*"; //$NON-NLS-1$
    private static final String pSEP = "|"; //$NON-NLS-1$
    private static final String pOPB = "("; //$NON-NLS-1$
    private static final String pCLB = ")"; //$NON-NLS-1$

    private static Pattern getPattern() {
        if (pattern == null) {
            // This small space-class addition is indeed a fix for CMP-31478
            pattern = Pattern.compile(pSPC
                    + pOPB + CatRefParserImpl.NR_REGEXP
                    + pSEP + CatRollupParser.CATROLLUP_REGEXP_NOT_GROUP
                    + pSEP + MTokenParser.MTOKEN_REGEXP_NOT_GROUP
                    + pCLB
                    + pSEP + LC_RULE
                    + pSEP + PATTERN_ATTRIBUTE
                    + pSEP + PATTERN_SYNTSX_WORD
                    + pSEP + PATTERN_SIMPLE_WORD
                    + pSEP + PATTERN_QUOTED_WORD
                    , Pattern.UNICODE_CHARACTER_CLASS);
        }
        return pattern;
    }
}
