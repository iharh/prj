plugins {
    id("cpp-library")
}

// val topRootDir: String by extra
val javacppGenDir: String by extra

// jni
val javaParent = file(System.getProperty("java.home")).getParentFile().getCanonicalFile()
val extractJavaDirForFile: (String) -> File = { fileName ->
    fileTree(javaParent).matching {
        include("**/$fileName") 
    }.getSingleFile().getParentFile()
}
val javaInclude = extractJavaDirForFile("jni.h")
val javaPlatformInclude = extractJavaDirForFile("jni_md.h")

library {
    // privateHeaders.from("$topRootDir/<some-dir>")

    tasks {
        withType<CppCompile>().configureEach {
            println("Configuring $name in project ${project.name} ...")

            dependsOn(":javaprovider:generateJavacpp")

            includes.from(javaInclude, javaPlatformInclude)

            source.from(fileTree(javacppGenDir).matching { include("*.cpp") })

            compilerArgs.addAll(listOf("-Werror", "-std=c++0x", "-D_GLIBCXX_USE_CXX11_ABI=0"))
        }
        //withType<LinkSharedLibrary>().configureEach {
        ////linkedFile.set(file("libabc.so"))
        //    libs.from(
        //        "$protobufInstallDir/lib/libprotobuf-lite.a",
        //        "$topBuildDir/libother.a"
        //    )
        //    // linkerArgs.addAll(['-Wl,--no-allow-shlib-undefined'])
        //}
    }
}
