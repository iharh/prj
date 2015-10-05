package antlr4;

import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwimlineParseTreeListener extends RuleBaseListener {
    private static final Logger log = LoggerFactory.getLogger(SwimlineParseTreeListener.class);

    private final Parser parser;

    private Set<String> simple;
    private Set<String> wildcard;
    private Set<String> quoted;
    private Set<String> mtokens;

    private int level;
    private boolean inValue;
    private boolean inMtoken;

    public SwimlineParseTreeListener(final Parser parser, Set<String> simple, Set<String> wildcard, Set<String> quoted, Set<String> mtokens, List<String> errors) {
        this.parser = parser;
        this.simple = simple;
        this.wildcard = wildcard;
        this.quoted = quoted;
        this.mtokens = mtokens;

        parser.removeErrorListeners();
        parser.addErrorListener(new SwimlineErrorListener(errors));
    }

    private void processSet(ParserRuleContext ctx, Set<String> s) {
        if (level != 1) {
            return;
        }
        final String v = ctx.getText();

        if (inValue && s != null) {
            s.add(v);
        } else if (inMtoken && mtokens != null) {
            mtokens.add(v);
        }
    }

    // cb_field/field
    
    @Override
    public void exitField(@NotNull RuleParser.FieldContext ctx) {
        final String v = ctx.getText();
        if ("_mtoken:".equalsIgnoreCase(v)) {
            inMtoken = true;
            //log.debug("exitField - mtoken found");
        }
    }

    @Override
    public void exitCb_field(@NotNull RuleParser.Cb_fieldContext ctx) {
        inMtoken = false;
    }

    // cb_value

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
        processSet(ctx, simple);
    }

    // truncated

    @Override
    public void exitTruncated(@NotNull RuleParser.TruncatedContext ctx) {
        log.debug("exit truncated level: {}", level);
        processSet(ctx, wildcard);
    }

    // quoted

    @Override
    public void enterQuoted(@NotNull RuleParser.QuotedContext ctx) {
        log.debug("enter quoted level: {}", level);
    }

    @Override
    public void exitQuoted(@NotNull RuleParser.QuotedContext ctx) {
        log.debug("exit quoted level: {}", level);
        processSet(ctx, quoted);
    }

    // quoted_truncated

    @Override
    public void exitQuoted_truncated(@NotNull RuleParser.Quoted_truncatedContext ctx) {
        log.debug("exit quoted_truncated level: {}", level);
        processSet(ctx, quoted);
    }
    
    // clause_or

    @Override
    public void enterClause_or(@NotNull RuleParser.Clause_orContext ctx) {
        ++level;
    }

    @Override
    public void exitClause_or(@NotNull RuleParser.Clause_orContext ctx) {
        --level;
    }

    // clause_not

    @Override
    public void enterClause_not(@NotNull RuleParser.Clause_notContext ctx) {
        //log.debug("enter clause_not");
        ++level;
    }
    
    @Override
    public void exitClause_not(@NotNull RuleParser.Clause_notContext ctx) {
        //log.debug("exit clause_not");
        --level;
    }
}

