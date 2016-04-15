parallelExecution := false

lazy val dstPath = settingKey[String]("Dst path")

lazy val copydf    = taskKey[Unit]("Dotfiles copy task")
lazy val copynotes = taskKey[Unit]("Notes copy task")
lazy val copyprj = taskKey[Unit]("Prj copy task")
lazy val copybin = taskKey[Unit]("Bin copy task")
lazy val copykeepass = taskKey[Unit]("KeePass copy task")
lazy val copyff = taskKey[Unit]("FF bookmarks copy task")

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
	    --- (vimfiles / "plugged" ***)
	    --- (vimfiles ** ".*")
	)
        +++ (src * "_*rc")
	+++ (src / ".gitconfig")
	//+++ (src / ".vimsauce" ***)
	//+++ (src / ".unite" / "bookmark" ***)
    )
    val rebasedFilesToCopy = filesToCopy pair Path.rebase(src, dst)
    // rebasedFilesToCopy foreach {tup => println(tup._2) }
    IO.copy(rebasedFilesToCopy)
    // IO.copyDirectory(src, dst)
} 

def copy_x_y(src: File, dst: File, name: String): Unit = {
    val cFiles = (src / name) ***
    val rFiles = cFiles pair Path.rebase(src, dst)
    //rFiles foreach {tup => println(tup._2) }
    IO.copy(rFiles)
}

def copy_x(n: String): Unit = {
    val dst = file("D:\\dev\\backup\\-\\backup") / n
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
    val dst = file("D:\\dev\\backup\\-\\backup\\auto\\KeePassDB")
    IO.delete(dst)
    IO.createDirectory(dst)

    val src = file("D:\\pvt\\backup\\KeePassDB")

    val cFiles = (src ) ***
    val rFiles = cFiles pair Path.rebase(src, dst)
    //rFiles foreach {tup => println(tup._2) }
    IO.copy(rFiles)
}

copyff := {
    val dst = file("D:\\dev\\backup\\-\\backup\\auto\\FFBookmarks")
    IO.delete(dst)
    IO.createDirectory(dst)

    val appdata = Path.userHome / "AppData/Roaming" // %APPDATA"
    val profile_hash = "jr2zyn95" // "1ifa5jee" // **
    val profile_name = "default"
    val profile = profile_hash + "." + profile_name
    val src = appdata / "Mozilla/Firefox/Profiles" / profile / "bookmarkbackups"
    val allBms = src * "*.jsonlz4" // sbt.ChildPathFinder
    val seqBms = allBms.get // Seq[File]
    val latestBm = seqBms.maxBy(_.lastModified)
    //println("max: " + latestBm)

    val filesToCopy: Seq[File] = latestBm :: Nil
    val rebasedFilesToCopy = filesToCopy pair Path.rebase(src, dst)
    //rebasedFilesToCopy foreach {tup => println(tup._2) }
    IO.copy(rebasedFilesToCopy)
}

addCommandAlias("copyall", ";copydf;copynotes;copyprj;copybin;copykeepass;copyff")
