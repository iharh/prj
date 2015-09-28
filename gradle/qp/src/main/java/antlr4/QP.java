package antlr4;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.ANTLRInputStream;
//import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.tree.ParseTree;
//import org.antlr.v4.runtime.tree.ParseTreeListener;

import java.io.Reader;
import java.io.StringReader;
import java.io.IOException;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QP {
    private static final Logger log = LoggerFactory.getLogger(QP.class);

    public static void parse(String inputString, Set<String> simple, Set<String> quoted) throws IOException {
        log.info("start");
        final Reader inputReader = new StringReader(inputString);
        final CharStream inputStream = new ANTLRInputStream(inputReader);
        final Lexer lexer = new RuleLexer(inputStream);

        final TokenStream tokenStream = new CommonTokenStream(lexer);

        final RuleParser /*Parser*/ parser = new RuleParser(tokenStream);
        //assertNotNull(parser);

        // ParseTreeListener 
        final SwimlineParseTreeListener parseTreeListener = new SwimlineParseTreeListener(parser, simple, quoted);
        parser.addParseListener(parseTreeListener);
        //parser.setTrace(true);

	//final RuleParser.Cb_ruleContext ctx = parser.cb_rule(); // throws RecognitionException
	//final ParseTree parseTree = parser.cb_rule(); // throws RecognitionException
        //log.info("ParseTree: {}", t.toStringTree(parser));
        //log.info("finish");
        parser.cb_rule();
    }
}
