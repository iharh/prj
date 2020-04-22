import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    id("java-library")
    id("io.spring.dependency-management").version("1.0.9.RELEASE").apply(false)
}
allprojects {
    repositories {
        mavenCentral()
        jcenter()
    }
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "io.spring.dependency-management")

    the<DependencyManagementExtension>().apply {
        dependencies {
            dependency("org.projectlombok:lombok:1.18.12")

            // dependency("com.google.guava:guava:28.0-jre")

            dependencySet("org.ow2.asm:8.0.1") { // 7.2
                entry("asm")
                entry("asm-commons")
            }

            dependency("org.eclipse.jetty:jetty-annotations:9.4.27.v20200227")
            dependency("org.jacoco:org.jacoco.core:0.8.5")
            dependency("com.github.jnr:jnr-ffi:2.1.12")
            dependency("net.minidev:accessors-smart:1.2")
            
            
            dependency("org.assertj:assertj-core:3.15.0")
            
            dependencySet("org.junit.jupiter:5.5.1") {
                entry("junit-jupiter-api")
                entry("junit-jupiter-engine")
            }
        }
    }

    tasks {
        compileJava {
            options.encoding = "UTF-8"
        }
        compileTestJava {
            options.encoding = "UTF-8"
        }
        test {
            useJUnitPlatform()

            testLogging {
                events("PASSED", "FAILED", "SKIPPED")
                showStandardStreams = true
            }
        }
    }

}
