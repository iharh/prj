lazy val compr = taskKey[Unit]("Compress task")

compr := {
    println("packing STARTED ...")
    val arcFile = "D:/compr.7z";
    val wrkBaseDirStr = "D:/Knova/-";
    val wrkBaseDir = file(wrkBaseDirStr)
    //Process("cmd /c 7z.bat --help > D:/compr.txt", wrkBaseDir).run()
    Process("cmd /c 7z.bat a -w -bd -mx=9 -- " + arcFile + " .", wrkBaseDir).run(true).exitValue()
    println("packing DONE.")
}


