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

//val wd: Path = pwd
val wd: Path = Path("/data/wrk/prj/am/my")
val r1 = %echo("abc")
val r2: CommandResult = %%echo("abc")
write.over(wd/"a.out", r2.out.lines)
// ? | read

//val r2 = Seq("l1", "l2")
//write.over(wd/"a.out", r2)

//https://github.com/lihaoyi/Ammonite/blob/master/ops/src/test/scala/test/ammonite/ops/ShelloutTests.scala
//implicit val clbWd = /'data/'wrk/'clb/'svnmain
//implicit val clbWd = pwd/'out
%ls(pwd/'out)
