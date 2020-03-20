plugins {
    id("cpp-application")
}

application {
    val thirdPartyDir = "${rootProject.getRootDir()}/../third_party"

    privateHeaders.from("$thirdPartyDir/icu4c/build/icu4c-inst/include")
    
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
                "$thirdPartyDir/icu4c/build/icu4c-inst/lib/libicuuc.a"
            )
            // linkerArgs.addAll(['-Wl,--no-allow-shlib-undefined'])
        }
    }
}
