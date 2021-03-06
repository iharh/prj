import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

repositories {
    mavenCentral()
}
plugins {
    //id("kotlin-dsl")
    `kotlin-dsl`
    id("io.spring.dependency-management").version("1.0.11.RELEASE")
}
kotlinDslPluginOptions {
    // gradle 7 does not use experimentall stuff anymore
    // experimentalWarning.set(false)
}
the<DependencyManagementExtension>().apply {
    dependencies {
        dependency("xalan:xalan:2.7.2")
    }
}
