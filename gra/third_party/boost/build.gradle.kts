import de.undercouch.gradle.tasks.download.Download
import de.undercouch.gradle.tasks.download.Verify

val boostVersion: String by project
val boostSha256: String by project
val boostBuildType: String by project

val boostVersionUnderscore = boostVersion.replace(".", "_")
val boostArchiveFile = File(buildDir, "boost-$boostVersion.tar.gz")
val boostBuildDir = "$buildDir/boost_$boostVersionUnderscore"
val boostInstDir = "$buildDir/boost-inst"

val icu4cInstDir="${rootProject.getRootDir()}/icu4c/build/icu4c-inst"

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
    }
    val runConfigure by registering(Exec::class) {
        dependsOn(extractArchive)

        workingDir(boostBuildDir)

        executable("./bootstrap.sh")
        args = listOf(
            "--prefix=$boostInstDir",
            "--with-toolset=gcc",
            "--with-icu=$icu4cInstDir",
            "--with-libraries=date_time,filesystem,system,thread"
        )
        inputs.file("$boostBuildDir/bootstrap.sh")
        outputs.file("$boostBuildDir/project-config.jam")

        doLast {
            mkdir(boostInstDir)
        }
    }
    val runBuild by registering(Exec::class) {
        dependsOn(runConfigure)

        workingDir(boostBuildDir)

        executable("./b2")
        args = listOf(
            "--build-type=minimal",
            "link=static",
            "variant=debug",
            "threading=multi",
            // "optimization=full",
            "install" 
        )
        inputs.file("$boostBuildDir/project-config.jam")
        outputs.file("$boostInstDir/lib/libboost_system.a")
    }
}
