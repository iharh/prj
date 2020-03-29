plugins {
    id("cpp-application")
}

application {
    privateHeaders.from("src/main/headers")
    
    tasks {
        withType<CppCompile>().configureEach {
            compilerArgs.addAll(listOf( 
                "-Wall",
                "-std=c++11"
            ))
            setPositionIndependentCode(true)
        }
    }
}
