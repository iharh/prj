import de.undercouch.gradle.tasks.download.Download
import de.undercouch.gradle.tasks.download.Verify

val xercescVersion: String by project
val xercescSha256: String by project
val xercescBuildType: String by project

val xercescArchiveFile = File(buildDir, "$xercescVersion.tar.gz")
val xercescSrcDir = "$buildDir/xerces-c-$xercescVersion"
val xercescBuildDir = "$xercescSrcDir/build"
val xercescInstDir = "$xercescBuildDir/inst"

tasks {
    val downloadArchive by registering(Download::class) {
        src("https://github.com/apache/xerces-c/archive/v${xercescVersion}.tar.gz")
        dest(xercescArchiveFile)
        overwrite(false)
    }
    val verifyArchive by registering(Verify::class) {
        dependsOn(downloadArchive)

        src(xercescArchiveFile)
        algorithm("SHA-256")
        checksum(xercescSha256)
    }
    val extractArchive by registering(Copy::class) {
        dependsOn(verifyArchive)

        from(tarTree(xercescArchiveFile))
        into(buildDir)

        doLast {
            mkdir(xercescBuildDir)
        }
    }
    val configureXercesc by registering(Exec::class) {
        dependsOn(extractArchive)

        workingDir(xercescBuildDir)
        executable("cmake")
        args = listOf(
            "-DCMAKE_INSTALL_PREFIX=inst",
            "-DBUILD_SHARED_LIBS:BOOL=OFF",
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
            "--config", xercescBuildType,
            "-t", "install"
        )
        inputs.file("$xercescBuildDir/Makefile")
        outputs.file("$xercescInstDir/lib/libxerces-c-3.2.a")
    }
    val versionCmake by registering(Exec::class) {
        executable("cmake")
        args = listOf("--version")
    }
}
