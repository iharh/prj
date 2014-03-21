lazy val trimall = taskKey[Unit]("Trim archives at all the backup places")

// TODO: move to commong .scala file
val archSrcDir = file("D:\\Knova")
val archDstDir1 = file("F:\\pvt\\Backup\\backup")
val archDstDir2 = file("\\\\EPBYGOMSA0000\\Personal") / System.getenv("USERNAME") / "-"
val archMask = "*.7z";
val dstSize = 10;

def archFiles(base: File): Seq[File] = {
    val fileFinder = base * archMask
    val fileSeq = fileFinder.get
    fileSeq.sortWith(_.lastModified() <= _.lastModified())
}

def archAndTrim(fileToArch: File, base: File): Unit = {
    // TODO: change color
    println(base / fileToArch.getName)
    IO.copyFile(fileToArch, base / fileToArch.getName)
    //
    val fileSeq = archFiles(base)
    val fileSeqSize = fileSeq.size
    if (fileSeqSize > dstSize) {
        val dropSize = fileSeqSize - dstSize
        //println("need to drop " + dropSize)
        fileSeq.take(dropSize) foreach {f => IO.delete(f) }
    }
}

trimall := {
    val fileToArch = archFiles(archSrcDir).last // head
    archAndTrim(fileToArch, archDstDir1)
    archAndTrim(fileToArch, archDstDir2)
}

