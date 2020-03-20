plugins {
    id("cpp-application")
}

application {
    val thirdPartyDir = "${rootProject.getRootDir()}/../third_party"
    val icuInstDir = "$thirdPartyDir/icu4c/build/icu4c-inst"

    privateHeaders.from("$icuInstDir/include")
    
    tasks {
        withType<CppCompile>().configureEach {
            compilerArgs.addAll(listOf( 
                "-Wall",        // "-Werror",
                "-std=c++11",   // "-std=c++0x"
                "-D_GLIBCXX_USE_CXX11_ABI=0"
            ))
            setPositionIndependentCode(true)
            // includes.from(javaInclude, javaPlatformInclude)
        }
        withType<LinkExecutable>().configureEach { // LinkSharedLibrary
            libs.from(
                "$icuInstDir/lib/libicuuc.a"
            )
            // linkerArgs.addAll(['-Wl,--no-allow-shlib-undefined'])
        }
    }
}
