val exposedSchemes: Configuration by configurations.creating {
    // This configuration is an "outgoing" configuration, it's not meant to be resolved
    isCanBeResolved = true
    // As an outgoing configuration, explain that consumers may want to consume it
    isCanBeConsumed = true
}

dependencies {
    exposedSchemes(files("$buildDir/classes") {
        builtBy("compileCustom")
    })
}

artifacts {
    add("exposedSchemes", compressScheme.archiveFile) {// tasks["compressScheme"]
        builtBy(compressScheme)
    }
}

tasks {
    register("compileCustom") {
        doLast {
            println("compiling custom classes")
        }
    }
    register("list") {
        dependsOn(exposedSchemes) // configurations["compileClasspath"]
        doLast {
            println("classpath = ${exposedSchemes.map { file: File -> file.name }}")
        }
    }
}
