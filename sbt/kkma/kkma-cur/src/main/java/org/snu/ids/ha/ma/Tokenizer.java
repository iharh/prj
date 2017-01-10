package org.snu.ids.ha.ma;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;


import java.lang.Character.UnicodeBlock;
import java.util.regex.Pattern;

import org.snu.ids.ha.util.Util;

public class Tokenizer {
    public static final TokenPattern[] PREDEFINED_TOKEN_PATTERN = new TokenPattern[]{
            new TokenPattern("[a-zA-Z0-9]+[-][a-zA-Z0-9]+", CharSetType.COMBINED),
            new TokenPattern("(ㄱ|ㄴ|ㄷ|ㄹ|ㅁ|ㅂ|ㅅ|ㅇ|ㅈ|ㅊ|ㅋ|ㅌ|ㅍ|ㅎ){10,}", CharSetType.COMBINED), // added for LANG-238
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


    public static List<Token> tokenize(String text) {
        if (!Util.valid(text)) {
            return null;
        }
        ArrayList<Token> result = new ArrayList<Token>();
        StringBuffer sb = new StringBuffer(text);

        int idx = 0;
        for(int num_patterns = PREDEFINED_TOKEN_PATTERN.length; idx < num_patterns; ++idx) {
            TokenPattern pat = PREDEFINED_TOKEN_PATTERN[idx];
            // get pattern matches (returned as a List of tokens) and erase them from the string-buffer
            List<Token> found = find(sb, pat);
            result.addAll(found);
        }

        int textLen = text.length();
        boolean[] var13 = checkFound(textLen, result);
        char var15 = 0;
        String var7 = "";
        CharSetType var8 = CharSetType.ETC;
        CharSetType var9 = CharSetType.ETC;
        int var10 = 0;

        for(int var11 = 0; var11 < textLen; ++var11) {
            char var14 = sb.charAt(var11);
            var9 = var8;
            UnicodeBlock var12 = UnicodeBlock.of(var14);
            if(var13[var11]) {
                var8 = CharSetType.EMOTICON;
            } else if(var12 != UnicodeBlock.HANGUL_SYLLABLES && var12 != UnicodeBlock.HANGUL_COMPATIBILITY_JAMO) {
                if(var12 != UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS && var12 != UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS) {
                    if((var14 < 65 || var14 > 90) && (var14 < 97 || var14 > 122)) {
                        if(var14 >= 48 && var14 <= 57) {
                            var8 = CharSetType.NUMBER;
                        } else if(var14 != 32 && var14 != 9 && var14 != 13 && var14 != 10) {
                            if(var12 != UnicodeBlock.LETTERLIKE_SYMBOLS && var12 != UnicodeBlock.CJK_COMPATIBILITY && var12 != UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION && var12 != UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS && var12 != UnicodeBlock.BASIC_LATIN) {
                                var8 = CharSetType.ETC;
                            } else {
                                var8 = CharSetType.SYMBOL;
                            }
                        } else {
                            var8 = CharSetType.SPACE;
                        }
                    } else {
                        var8 = CharSetType.ENGLISH;
                    }
                } else {
                    var8 = CharSetType.HANMUN;
                }
            } else {
                var8 = CharSetType.HANGUL;
            }

            if (var11 != 0 && (var9 != var8 || var8 == CharSetType.ETC && (var7.length() <= 0 || var7.charAt(var7.length() - 1) != var14) || var8 == CharSetType.SYMBOL && var15 != var14)) {
                if (var9 != CharSetType.EMOTICON) {
                    result.add(new Token(var7, var9, var10));
                }

                var10 = var11;
                var7 = "";
            }

            var7 = var7 + var14;
            var15 = var14;
            if (var12 == UnicodeBlock.HIGH_SURROGATES) {
                ++var11;
                var14 = sb.charAt(var11);
                var7 = var7 + var14;
                var15 = var14;
            }
        }

        if (Util.valid(var7)) {
            result.add(new Token(var7, var8, var10));
        }

        Collections.sort(result);
        return result;
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
