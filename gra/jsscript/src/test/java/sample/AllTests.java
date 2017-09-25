package sample;

import org.junit.jupiter.api.Test;

import org.xmlunit.matchers.CompareMatcher;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;

public class AllTests {
    private static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
    private static final Pattern XML_MATCHER = Pattern.compile("^<\\?xml\\s+.*?\\?>\\s*");

    @Test
    void test1() throws Exception {
        // String content = new String(Files.readAllBytes(Paths.get("readMe.txt")));
        String content = XML_HEADER + "<config xmlns=\"http://clarabridge.com/fx/config\"><module path=\"morph\"/></config>";
        String res = runScript(new File("config.en.js"), content);
        //assertEquals(XML_HEADER + "abc", res);
        //
        assertThat(res, CompareMatcher
            .isSimilarTo(XML_HEADER + "<config xmlns=\"http://clarabridge.com/fx/config\"><module path=\"morph5\"/></config>")
            .ignoreWhitespace()
        );
    }

    private static String runScript(final File script, final String content) throws Exception {
        final Context ctx = Context.enter();
        try {
            final Matcher m = XML_MATCHER.matcher(content);
            final boolean isXML = m.find();
            final String input = isXML ? m.replaceFirst("") : content;
            ctx.setLanguageVersion(Context.VERSION_DEFAULT);
            final Scriptable scope = ctx.initStandardObjects();
            ScriptableObject.putProperty(scope, "input", input);
            //ScriptableObject.putProperty(scope, "log", LOG);
            //ScriptableObject.putProperty(scope, "name", name);

            try (final Reader scriptReader = new BufferedReader(new FileReader(script))) {
                //LOG.info("evaluating: " + script.getAbsolutePath());
                ctx.evaluateReader(scope, scriptReader, script.getName(), 1, null);
            }

            final Object output = scope.get("output", scope);
            if (output == Scriptable.NOT_FOUND) {
                throw new Exception("The parameter 'output' is not defined after upgrade script '" + script + "' execution.");
            }
            return isXML ? XML_HEADER + output.toString() : output.toString();
        } finally {
            Context.exit();
        }
    }
}
