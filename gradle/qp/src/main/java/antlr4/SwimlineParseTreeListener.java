package antlr4;

import org.antlr.v4.runtime.Parser;

import java.util.Set;
import java.util.HashSet;

public class SwimlineParseTreeListener extends RuleBaseListener {
    private final Parser parser;

    private Set<String> values = new HashSet<>();

    private int level;

    public SwimlineParseTreeListener(final Parser parser) {
        this.parser = parser;
    }

    public Set<String> getValues() {
        return values;
    }

    // value

    @Override
    public void enterValue(RuleParser.ValueContext ctx) {
        // ctx at enterValue(...) does not have a text and finish-token
        //log.info("enter value level: {}", level);
    }

    @Override
    public void exitValue(RuleParser.ValueContext ctx) {
        if (level == 1) {
            values.add(ctx.getText());
        }
        //log.info("exit value level: {} text: {}", level, ctx.getText());
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

