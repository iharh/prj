parallelExecution := false

lazy val dstPath = settingKey[String]("Dst path")

lazy val copydf    = taskKey[Unit]("Dotfiles copy task")
lazy val copynotes = taskKey[Unit]("Notes copy task")
lazy val copyprj = taskKey[Unit]("Prj copy task")

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
	+++ (src / ".unite" / "bookmark" ***)
    )
    val rebasedFilesToCopy = filesToCopy pair Path.rebase(src, dst)
    // rebasedFilesToCopy foreach {tup => println(tup._2) }
    IO.copy(rebasedFilesToCopy)
} 

def copy_x(n: String): Unit = {
    val dst = file("D:\\Knova\\-\\backup") / n
    val src = file("D:\\dev") / n
    IO.delete(dst)
    IO.createDirectory(dst)
    val filesToCopy = (src / "wrk") ***
    val rebasedFilesToCopy = filesToCopy pair Path.rebase(src, dst)
    //rebasedFilesToCopy foreach {tup => println(tup._2) }
    IO.copy(rebasedFilesToCopy)
    //p1.lines.foreach(println)
    Process("cmd /c git archive HEAD -o " + dst / (n + ".tar"), src).run()
}

copynotes := {
    copy_x("notes")
    println("finish copynotes")
}

copyprj := {
    copy_x("prj")
    println("finish copyprj")
}

addCommandAlias("copyall", ";copydf;copynotes;copyprj")


