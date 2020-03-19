import de.undercouch.gradle.tasks.download.Download
import de.undercouch.gradle.tasks.download.Verify

val boostVersion: String by project
val boostSha256: String by project
val boostBuildType: String by project

val boostVersionUnderscore = boostVersion.replace(".", "_")
val boostArchiveFile = File(buildDir, "boost-$boostVersion.tar.gz")
val boostSrcDir = "$buildDir/boost-boost-$boostVersion"
val boostBuildDir = "$buildDir/boost-build"
val boostInstDir = "$buildDir/boost-inst"

tasks {
    val downloadArchive by registering(Download::class) {
        src("https://dl.bintray.com/boostorg/release/$boostVersion/source/boost_$boostVersionUnderscore.tar.gz")
        dest(boostArchiveFile)
        overwrite(false)
    }
    val verifyArchive by registering(Verify::class) {
        dependsOn(downloadArchive)

        src(boostArchiveFile)
        algorithm("SHA-256")
        checksum(boostSha256)
    }
    val extractArchive by registering(Copy::class) {
        dependsOn(verifyArchive)

        from(tarTree(boostArchiveFile))
        into(buildDir)

        doLast {
            mkdir(boostBuildDir)
        }
    }
    /*
    val runConfigure by registering(Exec::class) {
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
        outputs.file("$xercescInstDir/lib/libxerces-c-3.2.a")
    }
    */
    val versionCmake by registering(Exec::class) {
        executable("cmake")
        args = listOf("--version")
    }
}
