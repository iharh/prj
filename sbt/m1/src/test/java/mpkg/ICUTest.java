package mpkg;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import com.ibm.icu.text.Normalizer;
import com.ibm.icu.impl.UnicodeRegex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


import static org.hamcrest.CoreMatchers.is;

public class ICUTest {
    private static final String RU_STR = "превед,медвед";
    private static final String EN_STR = "w1ord,wo234r  d";

    private static final String AR_STR_1 = "خِ";
    private static final String AR_STR_2 = "خِدْمَة";
    private static final String AR_STR_3 = "مُمْتاز";
    private static final String COMA = ",";
    private static final String AR_STR = AR_STR_2 + COMA + AR_STR_3;

    private static final String PAT_ICU_1 = "[\\p{L}]";
    private static final String PAT_1 = "[\\p{javaLetterOrDigit}]";
    private static final String PAT_2 = "\\w+";
    private static final String PAT_3 = "[\\w\\?]+[\\w/\\*\\?\\s\\']*";

    private static final String AR_STR_DECOMP = "إِنَّ";
    private static final String AR_STR_COMP = "إِنَّ";
    @Test
    public void testNormalizerICU() throws Exception {
        assertEquals(AR_STR_COMP, Normalizer.compose(AR_STR_DECOMP, false));
    }

    @Test
    public void testRegexICU() throws Exception {
        // final Pattern pat = UnicodeRegex.compile(PAT_ICU_1); // fix-method is also interesting (LC, L, Nl)
        final Pattern pat = Pattern.compile(PAT_3, Pattern.UNICODE_CHARACTER_CLASS | Pattern.CASE_INSENSITIVE);

        assertEquals("", pat.matcher(AR_STR_1).replaceAll(""));
        assertEquals("", pat.matcher(AR_STR_2).replaceAll(""));
        assertEquals("", pat.matcher(AR_STR_3).replaceAll(""));
        assertEquals(COMA, pat.matcher(AR_STR).replaceAll(""));

        assertEquals(COMA, pat.matcher(RU_STR).replaceAll(""));

        assertEquals(COMA, pat.matcher(EN_STR).replaceAll(""));
    }
};
