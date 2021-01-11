import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    id("java-library")
    id("io.spring.dependency-management").version("1.0.9.RELEASE").apply(false)
    id("org.springframework.boot").version("2.2.11.RELEASE").apply(false)
}
allprojects {
    repositories {
        mavenCentral()
        jcenter()
    }
}
subprojects {
    apply(plugin = "java-library")
    apply(plugin = "idea")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")

    the<DependencyManagementExtension>().apply {
        dependencies {
            dependency("org.projectlombok:lombok:1.18.12")
            dependency("org.assertj:assertj-core:3.15.0");
        }
    }
    tasks {
        test {
            useJUnitPlatform()
            
            testLogging {
                events("PASSED", "FAILED", "SKIPPED")
                showStandardStreams = true
            }
        }
    }
}
