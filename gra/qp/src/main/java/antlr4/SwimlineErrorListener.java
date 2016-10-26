package antlr4;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Parser;

import java.util.List;
import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SwimlineErrorListener extends BaseErrorListener {
    private static final Logger log = LoggerFactory.getLogger(SwimlineErrorListener.class);

    List<String> errors;

    public SwimlineErrorListener(List<String> errors) {
        this.errors = errors;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer,
        Object offendingSymbol,
        int line, int charPositionInLine,
        String msg,
        RecognitionException rex
    ) {
        String errMsg = String.format("error line: %d:%d at %s msg: %s", line, charPositionInLine, offendingSymbol.toString(), msg);
        log.error("{}", errMsg);

        if (errors != null) {
            errors.add(errMsg);
        }

        List<String> stack = ((Parser)recognizer).getRuleInvocationStack();
        Collections.reverse(stack);
        log.error("error rule stack: {}", stack);
    }
}

