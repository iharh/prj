lazy val t1 = taskKey[Unit]("Echo some traverse task")

t1 := {
    val dstSize = 3
    val b = file("D:\\-obsolete-\\backup")
    val fileFinder = b * "*.7z"
    val fileSeq = fileFinder.get
    val fileSeqSize = fileSeq.size
    if (fileSeqSize > dstSize) {
        val dropSize = fileSeqSize - dstSize
        println("need to drop " + dropSize)
        fileSeq.sortWith(_.lastModified() <= _.lastModified()).take(dropSize) foreach {f => println("drop " + f) }
    } else {
        println("no drops needed")
    }
}

