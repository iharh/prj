package antlr4;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Set;
import java.util.HashSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwimlineParseTreeListener extends RuleBaseListener {
    private static final Logger log = LoggerFactory.getLogger(SwimlineParseTreeListener.class);

    private final Parser parser;

    private Set<String> values = new HashSet<>();

    private int level;
    private boolean inValue;

    public SwimlineParseTreeListener(final Parser parser) {
        this.parser = parser;
    }

    public Set<String> getValues() {
        return values;
    }

    // value

    @Override
    public void enterCb_value(@NotNull RuleParser.Cb_valueContext ctx) {
        inValue = true;
    }

    @Override
    public void exitCb_value(@NotNull RuleParser.Cb_valueContext ctx) {
        inValue = false;
    }
    
    @Override
    public void enterValue(@NotNull RuleParser.ValueContext ctx) {
        // ctx at enterValue(...) does not have a text and finish-token
        log.debug("enter value level: {}", level);
    }

    @Override
    public void exitValue(@NotNull RuleParser.ValueContext ctx) {
        if (inValue && level == 1) {
            values.add(ctx.getText());
        }
        log.debug("exit value inValue: {} level: {} text: {}", inValue, level, ctx.getText());
        //log.debug("exit value ctx: {}", ctx.toInfoString(parser));
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

