import org.gradle.api.tasks.JavaExec

import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    id("io.spring.dependency-management").version("1.0.11.RELEASE")
}
repositories {
    mavenCentral()
    jcenter()
}
the<DependencyManagementExtension>().apply {
    dependencies {
        dependency("org.mozilla:rhino:1.7.13")
    }
}

val cpRhino by configurations.creating

dependencies {
    cpRhino("org.mozilla:rhino")
}

tasks {
    register<JavaExec>("abc") {
        main = "org.mozilla.javascript.tools.shell.Main"
        classpath = cpRhino

        args(listOf(
            //"-help" "-?",
            // "-debug",
            "a.js"
        ))

        //jvmArgs("-Djava.library.path=$shLibDir")

        //environment(mapOf(
        //    "LD_LIBRARY_PATH" to shLibDir
        //))

        //inputs.dir("$globalSharedResDir/break-rule")
        //outputs.dir("$distResDir/break-rule")
    }
}
