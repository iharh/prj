import de.undercouch.gradle.tasks.download.Download
import de.undercouch.gradle.tasks.download.Verify

val xercescVersion: String by project
val xercescSha256: String by project

val xercescZipFile = File(buildDir, "${xercescVersion}.zip")

tasks {
    val downloadZip by registering(Download::class) {
        src("https://github.com/apache/xerces-c/archive/v${xercescVersion}.zip")
        dest(xercescZipFile)
        overwrite(false)
    }
    val verifyZip by registering(Verify::class) {
        dependsOn(downloadZip)
        src(xercescZipFile)
        algorithm("SHA-256")
        checksum(xercescSha256)
    }
    val unzipZip by registering(Copy::class) {
        dependsOn(verifyZip)
        from(zipTree(xercescZipFile))
        into(buildDir)
    }
}
