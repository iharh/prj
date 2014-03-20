lazy val trimall = taskKey[Unit]("Trim archives at all the backup places")

def trimAtFolder(base: File, mask: String, dstSize: Int): Unit = {
    val fileFinder = base * mask
    val fileSeq = fileFinder.get
    val fileSeqSize = fileSeq.size
    if (fileSeqSize > dstSize) {
        val dropSize = fileSeqSize - dstSize
        //println("need to drop " + dropSize)
        fileSeq.sortWith(_.lastModified() <= _.lastModified()).take(dropSize) foreach {f => IO.delete(f) }
    }
}

trimall := {
    trimAtFolder(file("F:\\pvt\\Backup\\backup"), "*.7z", 10)
    trimAtFolder(file("\\\\EPBYGOMSA0000\\Personal") / System.getenv("USERNAME") / "-", "*.7z", 10)
}

