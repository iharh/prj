plugins {
    id("cpp-application")
}

application {
    val thirdPartyDir = "${rootProject.getRootDir()}/../third_party"
    val icuInstDir = "$thirdPartyDir/icu4c/build/icu4c-inst"
    val xercescInstDir = "$thirdPartyDir/xercesc/build/xerces-c-inst"

    privateHeaders.from("$icuInstDir/include")
    privateHeaders.from("$xercescInstDir/include")
    
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
                "$xercescInstDir/lib/libxerces-c-3.2.a", // must go before icu
                "$icuInstDir/lib/libicuuc.a",
                "$icuInstDir/lib/libicudata.a"
                //"$icuInstDir/lib/libicuio.a",
                //"$icuInstDir/lib/libicutu.a",
            )
            // linkerArgs.addAll(['-Wl,--no-allow-shlib-undefined'])
        }
    }
}
