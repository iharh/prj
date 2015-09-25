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
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;


import java.io.Reader;
import java.io.StringReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class AntlrTest {
    private static final Logger log = LoggerFactory.getLogger(AntlrTest.class);

    private static class OurParseTreeListener extends RuleBaseListener {
        private final Parser parser;
        private int level;

        public OurParseTreeListener(final Parser parser) {
            this.parser = parser;
        }

        // value

        @Override
        public void enterValue(RuleParser.ValueContext ctx) {
            log.info("enter value level: {}", level);

        }

        @Override
        public void exitValue(RuleParser.ValueContext ctx) {
            // ctx at enterValue(...) does not have a text and finish-token
            log.info("exit value level: {} text: {}", level, ctx.getText());
            //log.info("exit value ctx: {}", ctx.toInfoString(parser));
        }

        //clause_or

	@Override
        public void enterClause_or(RuleParser.Clause_orContext ctx) {
            ++level;
        }

	@Override
        public void exitClause_or(RuleParser.Clause_orContext ctx) {
            --level;
        }
    }

    @Test
    public void testAntlr() throws Exception {
        log.info("start");

        //final String inputString = "abc";
        //final String inputString = "ab?c";
        final String inputString = "w1 or w2 or w3 and w4 and (w5 or w6 and (w7 and w8)) and w9 or (w10 and w11)";

        final Reader inputReader = new StringReader(inputString);
        final CharStream inputStream = new ANTLRInputStream(inputReader);
        final Lexer lexer = new RuleLexer(inputStream);

        final TokenStream tokenStream = new CommonTokenStream(lexer);

        final RuleParser /*Parser*/ parser = new RuleParser(tokenStream);
        assertNotNull(parser);

        final ParseTreeListener parseTreeListener = new OurParseTreeListener(parser);
        parser.addParseListener(parseTreeListener);
        //parser.setTrace(true);

	//final RuleParser.Cb_ruleContext ctx = parser.cb_rule(); // throws RecognitionException
        //assertNotNull(ctx);

	/*final ParseTree parseTree = */parser.cb_rule(); // throws RecognitionException
        //log.info("ParseTree: {}", t.toStringTree(parser));

        //assertTrue(true);
        log.info("finish");
    }
}

