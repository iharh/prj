package org.snu.ids.ha.ma;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;


import java.lang.Character.UnicodeBlock;
import java.util.regex.Pattern;

import org.snu.ids.ha.util.Util;

public class Tokenizer {
    private static final int MAX_DUP_CHAR_CNT = 7;

    public static final TokenPattern[] PREDEFINED_TOKEN_PATTERN = new TokenPattern[]{
            new TokenPattern("[a-zA-Z0-9]+[-][a-zA-Z0-9]+", CharSetType.COMBINED),
            //new TokenPattern("(ㄱ|ㄴ|ㄷ|ㄹ|ㅁ|ㅂ|ㅅ|ㅇ|ㅈ|ㅊ|ㅋ|ㅌ|ㅍ|ㅎ){10,}", CharSetType.COMBINED), // added for LANG-238
            new TokenPattern("(ㅋ|ㅠ|ㅜ|ㅎ){2,}", CharSetType.EMOTICON),
            new TokenPattern("(\\^){3,}", CharSetType.EMOTICON),
            new TokenPattern("[-]?[0-9]+([,][0-9]{3})*([.][0-9]+)?", CharSetType.NUMBER),
            new TokenPattern("[(][\\^]([.]|_|[-]|o|0|O|3|~|[ ])?[\\^][\']?[)]", CharSetType.EMOTICON),
            new TokenPattern("[d][\\^]([.]|_|[-]|o|0|O|3|~|[ ])?[\\^][b]", CharSetType.EMOTICON),
            new TokenPattern("[\\^]([.]|_|[-]|o|0|O|3|~|[ ])?[\\^]([;]+|[\'\"avVㅗ])?", CharSetType.EMOTICON),
            new TokenPattern("[(];_;[)]", CharSetType.EMOTICON),
            new TokenPattern("[(]T[_.~oO\\^]?T[)]", CharSetType.EMOTICON),
            new TokenPattern("ㅜ[_.]?ㅜ", CharSetType.EMOTICON),
            new TokenPattern("ㅡ[_.]?ㅜ", CharSetType.EMOTICON),
            new TokenPattern("ㅜ[_.]?ㅡ", CharSetType.EMOTICON),
            new TokenPattern("ㅠ[_.]?ㅠ", CharSetType.EMOTICON),
            new TokenPattern("ㅡ[_.]?ㅠ", CharSetType.EMOTICON),
            new TokenPattern("ㅠ[_.]?ㅡ", CharSetType.EMOTICON),
            new TokenPattern("ㅠ[_.]?ㅜ", CharSetType.EMOTICON),
            new TokenPattern("ㅜ[_.]?ㅠ", CharSetType.EMOTICON),
            new TokenPattern("[(][-](_|[.])?[-]([;]+|[aㅗ])?[)](zzZ)?", CharSetType.EMOTICON),
            new TokenPattern("[-](_|[.])?[-]([;]+|[aㅗ]|(zzZ))?", CharSetType.EMOTICON),
            new TokenPattern("[ㅡ](_|[.])?[ㅡ]([;]+|[aㅗ]|(zzZ))?", CharSetType.EMOTICON),
            new TokenPattern("[(][>]([.]|_)?[<][)]", CharSetType.EMOTICON),
            new TokenPattern("[>]([.]|_)?[<]", CharSetType.EMOTICON),
            new TokenPattern("[(][>]([.]|_)?[>][)]", CharSetType.EMOTICON),
            new TokenPattern("[>]([.]|_)?[>]", CharSetType.EMOTICON),
            new TokenPattern("[(][¬]([.]|_)?[¬][)]", CharSetType.EMOTICON),
            new TokenPattern("[¬]([.]|_)?[¬]", CharSetType.EMOTICON),
            new TokenPattern("[(]\'(_|[.])\\^[)]", CharSetType.EMOTICON),
            new TokenPattern("\'(_|[.])\\^", CharSetType.EMOTICON),
            new TokenPattern("\\^(_|[.])[~]", CharSetType.EMOTICON),
            new TokenPattern("[~](_|[.])\\^", CharSetType.EMOTICON),
            new TokenPattern("[(][.][_][.][)]", CharSetType.EMOTICON),
            new TokenPattern("[(][\'][_][\'][)]", CharSetType.EMOTICON),
            new TokenPattern("[(][,][_][,][)]", CharSetType.EMOTICON),
            new TokenPattern("[(][X][_][X][)]", CharSetType.EMOTICON),
            new TokenPattern("[O][_.][o]", CharSetType.EMOTICON),
            new TokenPattern("[o][_.][O]", CharSetType.EMOTICON),
            new TokenPattern("m[(]_ _[)]m", CharSetType.EMOTICON)};

    // Token
    //   protected String string;
    //   protected CharSetType charSet;
    //   protected int index;  // ? start pos in src text

    public static List<Token> tokenize(String text) {
        if (!Util.valid(text)) {
            return null;
        }
        ArrayList<Token> result = new ArrayList<Token>();
        StringBuffer sb = new StringBuffer(text);

        int num_patterns = PREDEFINED_TOKEN_PATTERN.length;
        for(int idx = 0; idx < num_patterns; ++idx) {
            TokenPattern pat = PREDEFINED_TOKEN_PATTERN[idx];
            // get pattern matches (returned as a List of tokens) and erase them from the string-buffer
            List<Token> found = find(sb, pat);
            result.addAll(found);
        }

        int textLen = text.length();
        boolean [] coveredByFoundTokenPattern = checkFound(textLen, result);
        char prevChar = 0;
        StringBuffer curToken = new StringBuffer("");
        CharSetType curCST = CharSetType.ETC;
        CharSetType prevCST = CharSetType.ETC;
        int curTokenIdx = 0;

        for (int idx = 0; idx < textLen; ++idx) {
            char curChar = sb.charAt(idx);
            prevCST = curCST;
            UnicodeBlock uBlock = UnicodeBlock.of(curChar);
            if (coveredByFoundTokenPattern[idx]) {
                curCST = CharSetType.EMOTICON;
            } else if (uBlock != UnicodeBlock.HANGUL_SYLLABLES && uBlock != UnicodeBlock.HANGUL_COMPATIBILITY_JAMO) {
                if (uBlock != UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS && uBlock != UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
                    if ((curChar < 65 || curChar > 90) && (curChar < 97 || curChar > 122)) {
                        if (curChar >= 48 && curChar <= 57) {
                            curCST = CharSetType.NUMBER;
                        } else if (curChar != 32 && curChar != 9 && curChar != 13 && curChar != 10) {
                            if (uBlock != UnicodeBlock.LETTERLIKE_SYMBOLS
                                && uBlock != UnicodeBlock.CJK_COMPATIBILITY
                                && uBlock != UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                                && uBlock != UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                                && uBlock != UnicodeBlock.BASIC_LATIN) {
                                curCST = CharSetType.ETC;
                            } else {
                                curCST = CharSetType.SYMBOL;
                            }
                        } else {
                            curCST = CharSetType.SPACE;
                        }
                    } else {
                        curCST = CharSetType.ENGLISH;
                    }
                } else {
                    curCST = CharSetType.HANMUN;
                }
            } else {
                curCST = CharSetType.HANGUL;
            }

            if (idx != 0 &&
                (prevCST != curCST
                 || curCST == CharSetType.ETC    && (curToken.length() <= 0 || curToken.charAt(curToken.length() - 1) != curChar)
                 || curCST == CharSetType.SYMBOL && prevChar != curChar)) {
                if (prevCST != CharSetType.EMOTICON) {
                    result.add(createTok(curToken, prevCST, curTokenIdx));
                }

                curTokenIdx = idx;
                curToken = new StringBuffer("");
            }

            curToken.append(curChar);
            prevChar = curChar;
            if (uBlock == UnicodeBlock.HIGH_SURROGATES) {
                ++idx;
                curChar = sb.charAt(idx);
                curToken.append(curChar);
                prevChar = curChar;
            }
        }

        if (Util.valid(curToken.toString())) {
            result.add(createTok(curToken, curCST, curTokenIdx));
        }

        Collections.sort(result);
        return result;
    }

    private static Token createTok(StringBuffer sb, CharSetType charSet, int index) {
        if (charSet != CharSetType.EMOTICON && charSet != CharSetType.COMBINED) {
            char prevC = 0;
            int dupCnt = 0;
            for (int idx = 0; idx < sb.length(); ++idx) {
                char curC = sb.charAt(idx);
                if (curC == prevC) {
                    ++dupCnt;
                } else {
                    dupCnt = 0;
                    prevC = curC;
                }
                if (dupCnt >= MAX_DUP_CHAR_CNT) {
                    charSet = CharSetType.COMBINED;
                    break;
                }
            }
        }
        return new Token(sb.toString(), charSet, index);
    }

    // get pattern matches (returned as a List of tokens) and erase them from the string-buffer
    private static List<Token> find(StringBuffer sb, TokenPattern tokenPat) {
        if (tokenPat == null) {
            return null;
        }
        ArrayList<Token> result = new ArrayList<Token>();
        Matcher m = tokenPat.pattern.matcher(sb);

        while (m.find()) {
            result.add(new Token(sb.substring(m.start(), m.end()), tokenPat.charSetType, m.start()));

            for (int idx = m.start(); idx < m.end(); ++idx) {
                sb.setCharAt(idx, ' ');
            }
        }
        return result;
    }

    // each char in text is marked wheter it has been found
    private static boolean[] checkFound(int textLen, List<Token> tokens) {
        boolean [] result = new boolean[textLen];

        for (int idx = 0; idx < textLen; ++idx) {
            result[idx] = false;
        }

        int tokensSize = (tokens == null ? 0 : tokens.size());
        for (int tokenIdx = 0; tokenIdx < tokensSize; ++tokenIdx) {
            Token tok = tokens.get(tokenIdx);

            int tokenStringLen = tok.string.length();
            for (int idx = 0; idx < tokenStringLen; ++idx) {
                result[tok.index + idx] = true;
            }
        }

        return result;
    }
}

class TokenPattern {
    Pattern pattern = null;
    CharSetType charSetType = null;

    TokenPattern(String strPattern, CharSetType charSetType) {
        this.pattern = Pattern.compile(strPattern);
        this.charSetType = charSetType;
    }
}
