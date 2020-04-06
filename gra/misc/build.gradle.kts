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
