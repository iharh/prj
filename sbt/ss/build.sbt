parallelExecution := false

lazy val dstPath = settingKey[String]("Dst path")

lazy val copydf    = taskKey[Unit]("Dotfiles copy task")
lazy val copynotes = taskKey[Unit]("Notes copy task")
lazy val copyprj = taskKey[Unit]("Prj copy task")
lazy val copybin = taskKey[Unit]("Bin copy task")
lazy val copykeepass = taskKey[Unit]("KeePass copy task")

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

def copy_x_y(src: File, dst: File, name: String): Unit = {
    val cFiles = (src / name) ***
    val rFiles = cFiles pair Path.rebase(src, dst)
    //rFiles foreach {tup => println(tup._2) }
    IO.copy(rFiles)
}

def copy_x(n: String): Unit = {
    val dst = file("D:\\Knova\\-\\backup") / n
    val src = file("D:\\dev") / n
    IO.delete(dst)
    IO.createDirectory(dst)

    copy_x_y(src, dst, "wrk")

    //p1.lines.foreach(println)
    Process("cmd /c git archive HEAD -o " + dst / (n + ".tar"), src).run()
    println("finish copy" + n)
}

copynotes := {
    copy_x("notes")
}

copyprj := {
    copy_x("prj")
}

copybin := {
    copy_x("bin")
}

copykeepass := {
    val dst = file("D:\\Knova\\-\\backup\\auto\\KeePassDB")
    IO.delete(dst)
    IO.createDirectory(dst)

    val src = file("F:\\pvt\\Backup\\KeePassDB")

    val cFiles = (src ) ***
    val rFiles = cFiles pair Path.rebase(src, dst)
    //rFiles foreach {tup => println(tup._2) }
    IO.copy(rFiles)
}

addCommandAlias("copyall", ";copydf;copynotes;copyprj;copybin;copykeepass")

//libraryDependencies ++= Seq(
//    "org.apache.commons" % "commons-compress" % "1.8"
//)
