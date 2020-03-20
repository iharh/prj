plugins {
    id("cpp-application")
}

application {
    tasks {
        withType<CppCompile>().configureEach {
            compilerArgs.addAll(listOf( 
                "-Wall",        // "-Werror",
                "-std=c++11",   // "-std=c++0x"
                "-D_GLIBCXX_USE_CXX11_ABI=0"
            ))
            setPositionIndependentCode(true)
        }
    }
}

//library {
//    tasks.withType(CppCompile).configureEach {
//        includes.from(javaInclude, javaPlatformInclude)
//    }
//}
