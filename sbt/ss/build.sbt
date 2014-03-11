import complete.DefaultParsers._
//import complete.Parser._

// http://www.scala-sbt.org/release/docs/Detailed-Topics/Parallel-Execution.html
//concurrentRestrictions in Global := Seq(
//  Tags.limitAll(1)
//  Tags.limit(Tags.CPU, 2),
//  Tags.limit(Tags.Network, 10),
//  Tags.limit(Tags.Test, 1),
//)
parallelExecution := false

lazy val dstPath = settingKey[String]("Dst path")

lazy val copydf    = taskKey[Unit]("Dotfiles copy task")
lazy val copynotes = taskKey[Unit]("Notes copy task")
lazy val copyprj = taskKey[Unit]("Prj copy task")

lazy val e0 = taskKey[Unit]("Echo some staff task 0")
lazy val e1 = taskKey[Unit]("Echo some staff task 1")
lazy val e2 = taskKey[Unit]("Echo some staff task 2")
lazy val e3 = taskKey[Unit]("Echo some staff task 3")

lazy val s1 = taskKey[Unit]("Echo some state task")
lazy val i1 = inputKey[Unit]("Some input task")

val argParser = OptSpace ~> token(StringBasic, "<arg>") // = spaceDelimited("<arg>")
// StringBasic.examples("<arg>") // : Parser[String]

dstPath := "D:\\dev\\bin\\dotfiles"

copydf := {
    val dst = file(dstPath.value)
    IO.delete(dst)
    IO.createDirectory(dst)
    // IO.copy(...).get map {f => (f, dst / f.getName)})
    val src = Path.userHome // .absolutePath
    val vimfiles = src / "vimfiles"
    val filesToCopy = (
        (
	    (vimfiles ***)
	    --- (vimfiles / "bundle" ***)
	    --- (vimfiles ** ".*")
	)
        +++ (src * "_*rc")
	+++ (src / ".vimsauce" ***)
    )
    val rebasedFilesToCopy = filesToCopy pair Path.rebase(src, dst)
    // rebasedFilesToCopy foreach {tup => println(tup._2) }
    IO.copy(rebasedFilesToCopy)
} 

copynotes := {
    val dst = file("D:\\Knova\\-\\backup\\notes")
    val src = file("D:\\dev\\notes")
    IO.delete(dst)
    IO.createDirectory(dst)
    val filesToCopy = (src / "wrk") ***
    val rebasedFilesToCopy = filesToCopy pair Path.rebase(src, dst)
    //rebasedFilesToCopy foreach {tup => println(tup._2) }
    IO.copy(rebasedFilesToCopy)
    //p1.lines.foreach(println)
    Process("cmd /c git archive HEAD -o " + dst / "notes.tar", src).run()
    println("finish copynotes")
}

copyprj := {
    val dst = file("D:\\Knova\\-\\backup\\prj")
    val src = file("D:\\dev\\prj")
    IO.delete(dst)
    IO.createDirectory(dst)
    val filesToCopy = (src / "wrk") ***
    val rebasedFilesToCopy = filesToCopy pair Path.rebase(src, dst)
    //rebasedFilesToCopy foreach {tup => println(tup._2) }
    IO.copy(rebasedFilesToCopy)
    //p1.lines.foreach(println)
    Process("cmd /c git archive HEAD -o " + dst / "prj.tar", src).run()
    println("finish copyprj")
}

e1 := {
    println("hello e1")
}

e2 := {
    println("hello e2")
    error("this is some error abc")
}

e3 := {
    println("hello e3")
}

e0 := Def.sequentialTask {
    println("hello e0 start")
    val _1 = e1.value
    val _2 = e2.value
    val _3 = e3.value
    println("hello e0 finish")
}.value

// e0 <<= e0.dependsOn(e3, e2, e1)

def ddd(v: String) = println(v)

s1 := {
    //ddd(dstPath.value)
}

// sbt i1"abc"
// sbt i1"""abc  def"""
// sbt i1"D:\dev\bin\dotfiles"
i1 := {
    println("before")
    val args = argParser.parsed // : Seq[String]
    println(args)
    println("after")
}

addCommandAlias("copyall", ";copydf;copynotes;copyprj")

addCommandAlias("eall", ";e1;e2;e3")

