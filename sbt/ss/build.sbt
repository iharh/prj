import complete.DefaultParsers._
//import complete.Parser._

lazy val dstPath = settingKey[String]("Dst path")
lazy val copydf    = taskKey[Unit]("Dotfiles copy task")
lazy val copynotes = taskKey[Unit]("Notes copy task")
lazy val e1 = taskKey[Unit]("Echo some staff task")
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
    val p1 = Process("cmd /c git archive HEAD -o " + dst / "notes.tar", src)
    p1.run()
}

e1 := {
    println("aaa")
}

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
