//import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import SanitizeExtension

plugins {
    id("idea")
    // id("io.spring.dependency-management").version("1.0.9.RELEASE").apply(false)
}

allprojects {
    repositories {
        mavenCentral()
        jcenter()
    }
}

val asan: String by extra

subprojects {
    // apply(plugin = "io.spring.dependency-management")
    apply(plugin = "sanitizer")

    the<SanitizeExtension>().apply {
        address = asan.toBoolean() && !project.name.startsWith("jni")
    }
}
