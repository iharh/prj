repositories {
    jcenter()
}
plugins {
    `kotlin-dsl`
}
kotlinDslPluginOptions {
    experimentalWarning.set(false)
}
gradlePlugin {
    plugins {
        register("sanitizer-plugin") {
            id = "sanitizer"
            implementationClass = "SanitizerPlugin"
        }
    }
}
