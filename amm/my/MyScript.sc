#! /usr/local/bin/amm
import $file.MyFmt, MyFmt._

import ammonite.ops.ImplicitWd._
//import ammonite.runtime.tools._

import org.joda.time.LocalDateTime 


val clbDir = Path("/data/wrk/clb")
val logbDir = clbDir/'logb
val svnDir = clbDir/'svnmain

val dt = LocalDateTime.now().toString(fmt)

val curLogDir = logbDir/s"build-$dt"
mkdir! curLogDir

// grep "Revision: "
// "hello" |> write.append! curLogDir/"a.out"

val strRev = "Revision: "
val svnRevOld = (%%.svn("info")(svnDir).out.lines |? (_.startsWith(strRev)))(0).substring(strRev.length())

s"svnRevOld: $svnRevOld\n" |> write.append! curLogDir/"a.out"
s"svnRevOld: $svnRevOld\n" |> write.append! curLogDir/"a.out"
