import de.undercouch.gradle.tasks.download.Download
import de.undercouch.gradle.tasks.download.Verify

val xercescVersion: String by project
val xercescSha256: String by project

val xercescZipFile = File(buildDir, "$xercescVersion.zip")
val xercescSrcDir = "$buildDir/xerces-c-$xercescVersion"
val xercescBuildDir = "$xercescSrcDir/build"
val xercescInstDir = "$xercescBuildDir/inst"

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

        doLast {
            mkdir(xercescBuildDir)
        }
    }
    val configureXercesc by registering(Exec::class) {
        dependsOn(unzipZip)

        workingDir(xercescBuildDir)
        executable("cmake")
        args = listOf(
            "-DCMAKE_BUILD_TYPE=Debug",
            "-DCMAKE_INSTALL_PREFIX=inst",
            "-B", xercescBuildDir,
            "-S", xercescSrcDir
        )
        inputs.file("$xercescSrcDir/CMakeLists.txt")
        outputs.file("$xercescBuildDir/Makefile")
        doLast {
            mkdir(xercescInstDir)
        }
    }
    val buildXercesc by registering(Exec::class) {
        dependsOn(configureXercesc)

        workingDir(xercescBuildDir)
        executable("cmake")
        args = listOf(
            "--build", xercescBuildDir,
            "-t", "install"
        )
        inputs.file("$xercescBuildDir/Makefile")
        outputs.file("$xercescInstDir/lib/libxerces-c.so")
    }
    val versionCmake by registering(Exec::class) {
        executable("cmake")
        args = listOf("--version")
    }
}