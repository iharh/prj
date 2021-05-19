import org.gradle.nativeplatform.Linkage
import org.gradle.language.cpp.tasks.CppCompile

library {
    linkage.set(listOf(Linkage.SHARED))

    privateHeaders.from(file("src/main/headers"))
}
tasks {
    withType<CppCompile>().configureEach {
        compilerArgs.addAll(listOf(
            "-Wall",
            "-std=c++11",
            "-DU_DEFINE_FALSE_AND_TRUE=1"
            // "-D_GLIBCXX_USE_CXX11_ABI=0"
        ))
        setPositionIndependentCode(true)
    }
}
