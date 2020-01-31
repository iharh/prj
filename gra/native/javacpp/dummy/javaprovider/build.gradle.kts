import org.gradle.internal.os.OperatingSystem

buildscript {
    val verJavacpp: String by extra
    
    dependencies {
        classpath("org.bytedeco:javacpp:$verJavacpp")
    }
}

val javacppGenDir: String by extra
// val javacppGenFile: File by extra

plugins {
    id("java-library")
}

dependencies {
    api("org.bytedeco:javacpp")

    testImplementation("org.assertj:assertj-core")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
}

val rootPrjName = rootProject.name
val providerLibName = "${rootPrjName.capitalize()}ProviderLib"
val providerLibClassName = "$rootPrjName.$providerLibName"
val compileJava = tasks.named<JavaCompile>("compileJava")

// called by jniprovider
task("generateJavacpp") {
    group = "Custom"

    dependsOn("compileJava")

    // should be full path of "javaprovider/build/javacpp"
    val destinationDirStr: String = compileJava.get().getDestinationDirectory().get().getAsFile().getAbsolutePath()

    doLast {
        org.bytedeco.javacpp.tools.Builder()
            .classPaths(destinationDirStr)
            .classesOrPackages(providerLibClassName)
            .outputDirectory(javacppGenDir)
            .deleteJniFiles(false)
            .compile(false)
            .build() // returns the array of produced files
    }
}

val nativeProviderPrj = project(":jniprovider")
val nativeProviderLibDir = "${nativeProviderPrj.buildDir}/lib/main/release"
val nativeProviderBuildTasks = nativeProviderPrj.getTasksByName("assembleRelease", false)

val testJavaCP = listOf(nativeProviderLibDir).joinToString(File.pathSeparator)

val javacppOsName = if (OperatingSystem.current().isMacOsX()) "macosx" else "linux"
val sharedLibSuffix = OperatingSystem.current().getSharedLibrarySuffix()

tasks {
    register<Copy>("fixNativeProviderLibName") {
        dependsOn(nativeProviderBuildTasks)

        from(nativeProviderLibDir) {
            include("*$sharedLibSuffix")
            rename { filename -> filename.replace("provider", providerLibName) }
        }
        into(nativeProviderLibDir)
    }
    test {
        dependsOn("fixNativeProviderLibName")

        systemProperty("org.bytedeco.javacpp.logger.debug", "true")
        systemProperty("java.library.path", testJavaCP)
        environment(mapOf("LD_LIBRARY_PATH" to testJavaCP))
        
        useJUnitPlatform()

        testLogging {
            events("PASSED", "FAILED", "SKIPPED")
            showStandardStreams = true
        }
    }
    jar {
        dependsOn("fixNativeProviderLibName")

        from(nativeProviderLibDir) {
            include("*jni$providerLibName$sharedLibSuffix")
            into("$rootPrjName/$javacppOsName-x86_64")
        }

        archiveBaseName.set("${rootPrjName}provider")
        archiveAppendix.set(javacppOsName)
    }
}
