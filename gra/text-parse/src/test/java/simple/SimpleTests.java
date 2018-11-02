package simple;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

//import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleTests {
    private String punctuationSeparators = "।?!";
    private String plainTextBreakPattern = "\\r?\\n(?:\\r?\\n|(?=(?:[ \\t]+(?:\\-|[ ]{2,}))))";

    private Pattern exclusivePattern;

    private Pattern makePattern(String patternText) {
        if (patternText != null && patternText.length() > 0) {
            return Pattern.compile(patternText);
        }
        return null;
    }

    @Test
    public void test1() throws Exception {
        String srcText = "নেই।নেই।";

        exclusivePattern = makePattern("[" + punctuationSeparators + "]|(" + plainTextBreakPattern + ")");
        assertNotNull(exclusivePattern);
    }
}
