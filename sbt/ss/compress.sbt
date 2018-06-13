import java.text.SimpleDateFormat
import java.util.Date

lazy val compr = taskKey[Unit]("Compress task")

compr := {
    println("packing STARTED ...")
    val d = new Date()
    val dateFmt = new SimpleDateFormat ("yyyy-MM-dd")
    val arcFileName = System.getenv("USERNAME") + "-" + dateFmt.format(d) + ".7z"
    val arcFile = file("D:/dev/backup") / arcFileName
    val wrkBaseDir = file("D:/dev/backup/-")
    // Process("cmd /c 7z.exe a -w -bd -mx=9 -- " + arcFile + " .", wrkBaseDir).run(true).exitValue()
    println("packing DONE.")
}
