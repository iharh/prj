#! /usr/bin/amm
import $file.MyFmt, MyFmt._

import org.joda.time.LocalDateTime 

val dt = LocalDateTime.now().toString(fmt)
println(s"now: $dt")
