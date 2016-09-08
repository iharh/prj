appender("CONSOLE", ConsoleAppender) {
    withJansi = true
    encoder(PatternLayoutEncoder) {
        pattern = "%d{HH:mm:ss.SSS} [%.10thread] %highlight(%.-1level) %cyan(%logger{15}) - %msg%n"
        //pattern = "[%thread] %highlight(%-5level) %cyan(%logger{15}) - %msg %n"
    }
}

appender("FILE", FileAppender) {
    file = "kkma-cur.log"
    append = true
    encoder(PatternLayoutEncoder) {
        //pattern = "%level %logger - %msg%n"
        pattern = "%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n"
    }
}

root(DEBUG, ["CONSOLE", "FILE"])
