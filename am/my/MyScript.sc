#! /usr/bin/amm
import $file.MyFmt, MyFmt._

import ammonite.ops._
import ammonite.ops.ImplicitWd._

import org.joda.time.LocalDateTime 

val dt = LocalDateTime.now().toString(fmt)
println(s"now: $dt")


//val d: Path = pwd/'out
//rm! d
//mkdir! d
//write(d/"file1.txt", "I am cow")
//%.ls()
//ls!

val wd: Path = pwd
val r1 = %echo("abc")
val r2: CommandResult = %%echo("abc")
write.over(wd/"a.out", r2.out.lines)
// ? | read

//val r2 = Seq("l1", "l2")
//write.over(wd/"a.out", r2)
