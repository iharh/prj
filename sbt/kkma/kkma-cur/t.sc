#! /usr/bin/amm

import $ivy.`joda-time:joda-time:2.9.4`

//import sys.process._
//import collection.mutable
import org.joda.time.LocalDateTime 
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.DateTimeFormatterBuilder

val sep = '-'
val fmt: DateTimeFormatter = new DateTimeFormatterBuilder()
    .appendYear(4, 4)     // .appendTwoDigitYear(1956)
    .appendLiteral(sep)
    .appendMonthOfYear(2) //.appendMonthOfYearShortText()
    .appendLiteral(sep)
    .appendDayOfMonth(2)
    .appendLiteral(sep)
    .appendClockhourOfDay(2)
    .appendLiteral(sep)
    .appendMinuteOfHour(2)
    .appendLiteral(sep)
    .appendSecondOfMinute(2)
    .toFormatter();

val dt = LocalDateTime.now().toString(fmt)
println(s"now: $dt")
