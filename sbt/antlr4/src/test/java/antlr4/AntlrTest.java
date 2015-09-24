package antlr4;

import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.ANTLRInputStream;
//import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTreeListener;


import java.io.Reader;
import java.io.StringReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AntlrTest {
    private static final Logger log = LoggerFactory.getLogger(AntlrTest.class);

    @Test
    public void testAntlr() throws Exception {
        log.info("start");

        final String inputString = "abc";
        final Reader inputReader = new StringReader(inputString);
        final CharStream inputStream = new ANTLRInputStream(inputReader);
        final Lexer lexer = new RuleLexer(inputStream);

        final TokenStream tokenStream = new CommonTokenStream(lexer);

        final Parser parser = new RuleParser(tokenStream);
        final ParseTreeListener ruleTreeListener = new RuleBaseListener() {
            @Override
            public void enterValue(RuleParser.ValueContext ctx) {
                log.info("enter v");
            }

            @Override
            public void exitValue(RuleParser.ValueContext ctx) {
                log.info("exit v");
            }
        };
        parser.addParseListener(ruleTreeListener);

        assertNotNull(parser);

        assertTrue(true);
        log.info("finish");
    }
}

