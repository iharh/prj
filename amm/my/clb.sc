#! /usr/local/bin/amm
import $file.MyFmt, MyFmt._
import $file.svn, svn._

import ammonite.ops.ImplicitWd._
//import ammonite.runtime.tools._

import org.joda.time.LocalDateTime 

val pwd = cfg.getString("pwd")
println(s"pwd: $pwd")

/*
val clbDir = Path("/data/wrk/clb")
val logbDir = clbDir/'logb
val svnDir = clbDir/'svnmain

val dt = LocalDateTime.now().toString(fmt)

val curLogDir = logbDir/s"build-$dt"
mkdir! curLogDir
val logBuild = curLogDir/"build.log"

// grep "Revision: "
// "hello" |> write.append! curLogDir/"a.out"

val strRev = "Revision: "
val svnRevOld = (%%.svn("info")(svnDir).out.lines |? (_.startsWith(strRev)))(0).substring(strRev.length()).toInt
s"svnRevOld: $svnRevOld\n" |> write.append! logBuild

%%.svn("up", "-q")(svnDir).out.lines |> write.append! logBuild

val svnRevNew = (%%.svn("info")(svnDir).out.lines |? (_.startsWith(strRev)))(0).substring(strRev.length()).toInt
s"svnRevNew: $svnRevNew\n" |> write.append! logBuild

if (svnRevOld == svnRevNew) {
    "No svn changes found\n" |> write.append! logBuild
} else {
    //set /A old_revision+=1
    %%.svn("log", "-v", "-r", s"$svnRevOld:$svnRevNew")(svnDir).out.lines | (_ + "\n") |> write.append! logBuild
}
*/
