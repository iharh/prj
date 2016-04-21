import static ch.qos.logback.classic.Level.INFO
import static ch.qos.logback.classic.Level.DEBUG

import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender

appender("CONSOLE", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = "%-4relative [%thread] - %msg%n"
    }
}

root(DEBUG, ["CONSOLE"])

//<configuration>
// <include resource="org/springframework/boot/logging/logback/defaults.xml"/ -->
// <include resource="org/springframework/boot/logging/logback/console-appender.xml"/ -->
//    <root level="DEBUG">
//        <appender-ref ref="CONSOLE"/>
//    </root>
//</configuration>
