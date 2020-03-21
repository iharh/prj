import de.undercouch.gradle.tasks.download.Download
import de.undercouch.gradle.tasks.download.Verify

val xercescVersion: String by project
val xercescSha256: String by project
val xercescBuildType: String by project

val xercescArchiveFile = File(buildDir, "$xercescVersion.tar.gz")
val xercescSrcDir = "$buildDir/xerces-c-$xercescVersion"
val xercescBuildDir = "$buildDir/xerces-c-build"
val xercescInstDir = "$buildDir/xerces-c-inst"

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
    val runConfigure by registering(Exec::class) {
        dependsOn(extractArchive)

        workingDir(xercescBuildDir)
        executable("cmake")
        args = listOf(
            "-DCMAKE_INSTALL_PREFIX=$xercescInstDir",
            "-DBUILD_SHARED_LIBS:BOOL=OFF",
            "-Dnetwork-accessor=socket",
            "-B", xercescBuildDir,
            "-S", xercescSrcDir
        )
        inputs.file("$xercescSrcDir/CMakeLists.txt")
        outputs.file("$xercescBuildDir/Makefile")
        doLast {
            mkdir(xercescInstDir)
        }
    }
    val runBuild by registering(Exec::class) {
        dependsOn(runConfigure)

        workingDir(xercescBuildDir)
        executable("cmake")
        args = listOf(
            "--build", xercescBuildDir,
            "--config", xercescBuildType,
            "-t", "install"
        )
        inputs.file("$xercescBuildDir/Makefile")
        // outputs.file("$xercescInstDir/lib/libxerces-c-3.2.a")
    }
    val versionCmake by registering(Exec::class) {
        executable("cmake")
        args = listOf("--version")
    }
}
