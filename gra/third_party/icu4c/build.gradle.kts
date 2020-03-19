import de.undercouch.gradle.tasks.download.Download
import de.undercouch.gradle.tasks.download.Verify

val icu4cVersion: String by project
val icu4cSha256: String by project
// val icu4cBuildType: String by project

val icu4cVersionUnderscore = icu4cVersion.replace(".", "_")
val icu4cVersionDash = icu4cVersion.replace(".", "-")
val icu4cArchiveFile = File(buildDir, "icu4c-$icu4cVersionUnderscore-src.tgz") // ".zip" is for win
val icu4cSrcDir = "$buildDir/icu/source"
val icu4cBuildDir = "$buildDir/icu4c-build"
//val icu4cInstDir = "$icu4cBuildDir/inst"

tasks {
    create("printVer") {
        doLast {
            println("icu4cVersion: $icu4cVersion")
        }
    }
    val downloadArchive by registering(Download::class) {
        src("https://github.com/unicode-org/icu/releases/download/release-$icu4cVersionDash/icu4c-$icu4cVersionUnderscore-src.tgz")
        dest(icu4cArchiveFile)
        overwrite(false)
    }
    val verifyArchive by registering(Verify::class) {
        dependsOn(downloadArchive)

        src(icu4cArchiveFile)
        algorithm("SHA-256")
        checksum(icu4cSha256)
    }
    val extractArchive by registering(Copy::class) {
        dependsOn(verifyArchive)

        from(tarTree(icu4cArchiveFile))
        into(buildDir)

        doLast {
            mkdir(icu4cBuildDir)
        }
    }
    val runConfigure by registering(Exec::class) {
        dependsOn(extractArchive)

        workingDir(icu4cBuildDir)
        executable("$icu4cSrcDir/runConfigureICU")
        args = listOf("Linux")

        inputs.file("$icu4cSrcDir/runConfigureICU")
        outputs.file("$icu4cBuildDir/Makefile")
        //doLast {
        //    mkdir(icu4cInstDir)
        //}
    }
    /*
    val runBuild by registering(Exec::class) {
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
    */
}
