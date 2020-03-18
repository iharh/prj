import de.undercouch.gradle.tasks.download.Download
import de.undercouch.gradle.tasks.download.Verify

val xercescVersion: String by project
val xercescSha256: String by project

tasks {
    val downloadZip by registering(Download::class) {
        src("https://github.com/apache/xerces-c/archive/v${xercescVersion}.zip")
        dest(File(buildDir, "${xercescVersion}.zip"))
        overwrite(false)
    }
    val verifyZip by registering(Verify::class) {
        dependsOn(downloadZip)
        src(File(buildDir, "${xercescVersion}.zip"))
        algorithm("SHA-256")
        checksum(xercescSha256)
    }
}
