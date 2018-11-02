package simple;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.List;

public class SimpleTests {
    private String punctuationSeparators = "।?!";
    private String plainTextBreakPattern = "\\r?\\n(?:\\r?\\n|(?=(?:[ \\t]+(?:\\-|[ ]{2,}))))";
    private Pattern exclusivePattern = makePattern(plainTextBreakPattern, punctuationSeparators);

    private static Pattern makePattern(String patternText, String punctuationSeparators) {
        if (patternText != null && patternText.length() > 0) {
            if (punctuationSeparators != null && punctuationSeparators.length() > 0) {
                patternText = "[" + punctuationSeparators + "]|(" + patternText + ")";
            }
            return Pattern.compile(patternText);
        }
        return null;
    }

    private int correctSentenceBound(final CharSequence sourceText, int pos) {
        int result = pos;
        while (result < sourceText.length()) {
            char ch = sourceText.charAt(result);
            int idx = punctuationSeparators.indexOf(ch);
            dbg("correctSentenceBound pos: " + result + " ch: " + ch + " idx: " + idx);
            if (idx < 0) {
                break;
            } else {
                ++result;
                dbg("correctSentenceBound pos: corrected");
            }
        }
        return result;
    }

    private void dbg(String str) {
        if (true) {
            System.out.println(str);
        }
    }

    private void addChain(List<String> result, final CharSequence sourceText, int start, int end) {
        final String str = sourceText.subSequence(start, end).toString();
        dbg("adding " + start + " - " + end + " str: " + str);
        result.add(str);
    }

    public List<String> parseWithPattern(Pattern pattern, final CharSequence sourceText) {
        List<String> result = new ArrayList<>();

        Matcher m = pattern.matcher(sourceText);
        int start = 0;
        while (m.find()) {
            int end = m.start();
            if (end > start) {
                int newEnd = end;
                if (punctuationSeparators != null && punctuationSeparators.length() > 0) {
                    newEnd = correctSentenceBound(sourceText, end);
                }
                addChain(result, sourceText, start, newEnd);
            }
            start = m.end();
        }

        int end = sourceText.length();
        if (start < end) {
            //Process remaining text (or all the text if breaks are not found).
            addChain(result, sourceText, start, end);
        }
        return result;
    }

    @Test
    public void test1() throws Exception {
        assertThat(parseWithPattern(exclusivePattern, "নেই।নেই।")).containsExactly("নেই।", "নেই।");
        assertThat(parseWithPattern(exclusivePattern, "নেই।।।নেই।")).containsExactly("নেই।।।", "নেই।");
        assertThat(parseWithPattern(exclusivePattern, "নেই। নেই।")).containsExactly("নেই।", " নেই।");
        assertThat(parseWithPattern(exclusivePattern, "নেই।?!নেই")).containsExactly("নেই।?!", "নেই");
        assertThat(parseWithPattern(exclusivePattern, "নেই।\rনেই")).containsExactly("নেই।", "\rনেই");
        assertThat(parseWithPattern(exclusivePattern, "নেই।\n\nনেই")).containsExactly("নেই।", "নেই");
        assertThat(parseWithPattern(exclusivePattern, "নেই\n\nনেই")).containsExactly("নেই", "নেই");
        // parseWithPattern(exclusivePattern, "নেই।\n\nনেই");
        // assertTrue(true);
    }
}
