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
def LOG_FILE = 'app.log'
def LOG_PATTERN = '%d %5p [%.32t] \\(%logger{32}\\) - %m%n'
// https://logback.qos.ch/manual/layouts.html#Parentheses
// %F:%L are incorrect because of the grizzled-slf4j macro expansion stuff

appender('STAT_FILE', RollingFileAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "${LOG_PATTERN}"
    }
    file = LOG_FILE

    rollingPolicy(FixedWindowRollingPolicy) {
        fileNamePattern = "${LOG_FILE}.%i"
    }
    triggeringPolicy(SizeBasedTriggeringPolicy) {
        maxFileSize = '100MB'
    }
}
root(INFO, ['STAT_FILE'])
//root(DEBUG, ["CONSOLE"])

// <include resource="org/springframework/boot/logging/logback/defaults.xml"/ -->
// <include resource="org/springframework/boot/logging/logback/console-appender.xml"/ -->
