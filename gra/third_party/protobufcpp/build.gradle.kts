import de.undercouch.gradle.tasks.download.Download
import de.undercouch.gradle.tasks.download.Verify

val protobufVersion: String by project
val protobufSha256: String by project
val protobufBuildType: String by project

val protobufCppArchiveFile = File(buildDir, "$protobufVersion.tar.gz")
val protobufCppSrcDir = "$buildDir/protobuf-$protobufVersion"
val protobufCppBuildDir = "$buildDir/protobuf-cpp-build"
val protobufCppInstDir = "$buildDir/protobuf-cpp-inst"

tasks {
    val downloadArchive by registering(Download::class) {
        src("https://github.com/protocolbuffers/protobuf/releases/download/v$protobufVersion/protobuf-cpp-$protobufVersion.tar.gz")
        dest(protobufCppArchiveFile)
        overwrite(false)
    }
    val verifyArchive by registering(Verify::class) {
        dependsOn(downloadArchive)

        src(protobufCppArchiveFile)
        algorithm("SHA-256")
        checksum(protobufSha256)
    }
    val extractArchive by registering(Copy::class) {
        dependsOn(verifyArchive)

        from(tarTree(protobufCppArchiveFile))
        into(buildDir)

        doLast {
            mkdir(protobufCppBuildDir)
        }
    }
    val runConfigure by registering(Exec::class) {
        dependsOn(extractArchive)
        workingDir(protobufCppBuildDir)

        executable("cmake")
        args = listOf(
            "-DCMAKE_INSTALL_PREFIX=$protobufCppInstDir",
            "-DCMAKE_BUILD_TYPE=$protobufBuildType",
            "-DBUILD_SHARED_LIBS:BOOL=OFF",
            "-DCMAKE_POSITION_INDEPENDENT_CODE:BOOL=ON",
            "-DCMAKE_VERBOSE_MAKEFILE:BOOL=ON",
            "-B", protobufCppBuildDir,
            "-S", "$protobufCppSrcDir/cmake"
        )

        inputs.file("$protobufCppSrcDir/cmake/CMakeLists.txt")
        outputs.file("$protobufCppBuildDir/Makefile")

        doLast {
            mkdir(protobufCppInstDir)
        }
    }
    val runBuild by registering(Exec::class) {
        dependsOn(runConfigure)
        workingDir(protobufCppBuildDir)

        executable("cmake")
        args = listOf(
            "--build", protobufCppBuildDir,
            "--config", protobufBuildType,
            "-t", "install"
        )

        inputs.file("$protobufCppBuildDir/Makefile")
        outputs.file("$protobufCppInstDir/lib/libprotobuf.a")
    }
}
