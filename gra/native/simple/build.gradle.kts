plugins {
    id("cpp-application")
}

application {
    // Define toolchain-specific compiler options

    binaries.configureEach() { // CppStaticLibrary::class.java) {
        println("binaries toolchain: ${getToolChain()}")
        when (toolChain) {
            is Gcc -> {
                println("gcc toolchain detected")
                (toolChain as Gcc).eachPlatform(p:GccPlatformToolChain -> {
                    println("hello me") // linker.executable = 'linker'
                })
            }
        }
    }

    val thirdPartyDir = "${rootProject.getRootDir()}/../third_party"
    val icuInstDir = "$thirdPartyDir/icu4c/build/icu4c-inst"
    val xercescInstDir = "$thirdPartyDir/xercesc/build/xerces-c-inst"

    privateHeaders.from("$icuInstDir/include")
    privateHeaders.from("$xercescInstDir/include")
    
    tasks {
        withType<CppCompile>().configureEach {
            compilerArgs.addAll(listOf( 
                "-Wall",        // "-Werror",
                "-std=c++11"    // "-std=c++0x"
                // "-D_GLIBCXX_USE_CXX11_ABI=0"
            ))
            setPositionIndependentCode(true)
            // includes.from(javaInclude, javaPlatformInclude)

            // no value
            // println("cppcompile toolchain: ${getToolChain().get()}")
        }
        withType<LinkExecutable>().configureEach { // LinkSharedLibrary
            libs.from( // !!! order is very important !!!
                "$xercescInstDir/lib/libxerces-c-3.2.a",
                "$icuInstDir/lib/libicui18n.a",
                "$icuInstDir/lib/libicuuc.a",
                "$icuInstDir/lib/libicudata.a"
                //"$icuInstDir/lib/libicuio.a"
                //"$icuInstDir/lib/libicutu.a"
                //"$icuInstDir/lib/libicutest.a"
            )
            // linkerArgs.addAll(['-Wl,--no-allow-shlib-undefined'])
        }
    }
}
