import static ch.qos.logback.classic.Level.INFO
import static ch.qos.logback.classic.Level.DEBUG

import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy

appender('CONSOLE', ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = '%-4relative [%thread] - %msg%n'
    }
}

//def LOG_FILE = "${LOG_FILE:-${LOG_PATH:-${LOG_TEMP:-${java.io.tmpdir:-/tmp}}/}app.log}"
def LOG_FILE = 'cld2test.log'

appender('LOG_FILE', RollingFileAppender) {
    encoder(PatternLayoutEncoder) {
        //pattern = "${FILE_LOG_PATTERN}"
        pattern = '%msg%n'
    }
    file = "${LOG_FILE}"

    rollingPolicy(FixedWindowRollingPolicy) {
        fileNamePattern = "${LOG_FILE}.%i"
    }
    triggeringPolicy(SizeBasedTriggeringPolicy) {
        maxFileSize = '100MB'
    }
}
root(INFO, ['LOG_FILE'])
//root(DEBUG, ["CONSOLE"])

//def ERR_LOG_FILE = 'err.log'

// <include resource="org/springframework/boot/logging/logback/defaults.xml"/ -->
// <include resource="org/springframework/boot/logging/logback/console-appender.xml"/ -->
