package antlr4;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwimlineParseTreeListener extends RuleBaseListener {
    private static final Logger log = LoggerFactory.getLogger(SwimlineParseTreeListener.class);

    private final Parser parser;

    private Set<String> simple;
    private Set<String> quoted;

    private int level;
    private boolean inValue;

    public SwimlineParseTreeListener(final Parser parser, Set<String> simple, Set<String> quoted) {
        this.parser = parser;
        this.simple = simple;
        this.quoted = quoted;
    }

    // value

    @Override
    public void enterCb_value(@NotNull RuleParser.Cb_valueContext ctx) {
        log.debug("enter cb_value level: {}", level);
        inValue = true;
    }

    @Override
    public void exitCb_value(@NotNull RuleParser.Cb_valueContext ctx) {
        inValue = false;
        log.debug("exit cb_value level: {}", level);
    }
    
    // normal

    @Override
    public void enterNormal(@NotNull RuleParser.NormalContext ctx) {
        // ctx at enterNormal(...) does not have a text and finish-token
        log.debug("enter normal level: {}", level);
    }

    @Override
    public void exitNormal(@NotNull RuleParser.NormalContext ctx) {
        log.debug("exit normal inValue: {} level: {} text: {}", inValue, level, ctx.getText());
        //log.debug("exit normal ctx: {}", ctx.toInfoString(parser));
        if (inValue && level == 1) {
            simple.add(ctx.getText());
        }
    }

    // truncated

    @Override
    public void enterTruncated(@NotNull RuleParser.TruncatedContext ctx) {
        log.debug("enter truncated level: {}", level);
    }

    @Override
    public void exitTruncated(@NotNull RuleParser.TruncatedContext ctx) {
        log.debug("exit truncated level: {}", level);
        if (inValue && level == 1) {
            simple.add(ctx.getText());
        }
    }

    // quoted

    @Override
    public void enterQuoted(@NotNull RuleParser.QuotedContext ctx) {
        log.debug("enter quoted level: {}", level);
    }

    @Override
    public void exitQuoted(@NotNull RuleParser.QuotedContext ctx) {
        log.debug("exit quoted level: {}", level);
    }

    // quoted_truncated

    @Override
    public void enterQuoted_truncated(@NotNull RuleParser.Quoted_truncatedContext ctx) {
        log.debug("enter quoted_truncated level: {}", level);
    }

    @Override
    public void exitQuoted_truncated(@NotNull RuleParser.Quoted_truncatedContext ctx) {
        log.debug("exit quoted_truncated level: {}", level);
        if (inValue && level == 1) {
            quoted.add(ctx.getText());
        }
    }
    
    //clause_or

    @Override
    public void enterClause_or(@NotNull RuleParser.Clause_orContext ctx) {
        ++level;
    }

    @Override
    public void exitClause_or(@NotNull RuleParser.Clause_orContext ctx) {
        --level;
    }
}

