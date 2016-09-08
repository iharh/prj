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


    public static List<Token> tokenize(String var0) {
        if(!Util.valid(var0)) {
            return null;
        } else {
            ArrayList var1 = new ArrayList();
            StringBuffer var2 = new StringBuffer(var0);
            int var3 = 0;

            for(int var4 = PREDEFINED_TOKEN_PATTERN.length; var3 < var4; ++var3) {
                TokenPattern var5 = PREDEFINED_TOKEN_PATTERN[var3];
                List var6 = find(var2, var5);
                var1.addAll(var6);
            }

            var3 = var0.length();
            boolean[] var13 = checkFound(var3, var1);
            char var15 = 0;
            String var7 = "";
            CharSetType var8 = CharSetType.ETC;
            CharSetType var9 = CharSetType.ETC;
            int var10 = 0;

            for(int var11 = 0; var11 < var3; ++var11) {
                char var14 = var2.charAt(var11);
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

                if(var11 != 0 && (var9 != var8 || var8 == CharSetType.ETC && (var7.length() <= 0 || var7.charAt(var7.length() - 1) != var14) || var8 == CharSetType.SYMBOL && var15 != var14)) {
                    if(var9 != CharSetType.EMOTICON) {
                        var1.add(new Token(var7, var9, var10));
                    }

                    var10 = var11;
                    var7 = "";
                }

                var7 = var7 + var14;
                var15 = var14;
                if(var12 == UnicodeBlock.HIGH_SURROGATES) {
                    ++var11;
                    var14 = var2.charAt(var11);
                    var7 = var7 + var14;
                    var15 = var14;
                }
            }

            if(Util.valid(var7)) {
                var1.add(new Token(var7, var8, var10));
            }

            Collections.sort(var1);
            return var1;
        }
    }

    private static List<Token> find(StringBuffer var0, TokenPattern var1) {
        if(var1 == null) {
            return null;
        } else {
            ArrayList var2 = new ArrayList();
            Matcher var3 = var1.pattern.matcher(var0);

            while(var3.find()) {
                var2.add(new Token(var0.substring(var3.start(), var3.end()), var1.charSetType, var3.start()));

                for(int var4 = var3.start(); var4 < var3.end(); ++var4) {
                    var0.setCharAt(var4, ' ');
                }
            }

            return var2;
        }
    }

    private static boolean[] checkFound(int var0, List<Token> var1) {
        boolean[] var2 = new boolean[var0];

        int var3;
        for(var3 = 0; var3 < var0; ++var3) {
            var2[var3] = false;
        }

        var3 = 0;

        for(int var4 = var1 == null?0:var1.size(); var3 < var4; ++var3) {
            Token var5 = (Token)var1.get(var3);
            int var6 = 0;

            for(int var7 = var5.string.length(); var6 < var7; ++var6) {
                var2[var5.index + var6] = true;
            }
        }

        return var2;
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