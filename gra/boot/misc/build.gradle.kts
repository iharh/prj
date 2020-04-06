import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    // val springBootVersion = "2.2.6.RELEASE"
    id("java-library")
    id("idea")
    // https://plugins.gradle.org/plugin/io.spring.dependency-management
    id("io.spring.dependency-management").version("1.0.9.RELEASE").apply(false)
    // id("org.springframework.boot").version(springBootVersion).apply(false)
    id("com.github.ben-manes.versions").version("0.28.0").apply(false)
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
    // apply(plugin = "org.springframework.boot")
    apply(plugin = "com.github.ben-manes.versions")

    the<DependencyManagementExtension>().apply {
        dependencies {
            dependency("org.projectlombok:lombok:1.18.12")

            // dependency("com.google.guava:guava:28.2-jre")

            dependency("org.yaml:snakeyaml:1.26")

            dependency("javax.cache:cache-api:1.1.1")
            dependency("org.ehcache:ehcache:3.8.1")

            dependency("org.assertj:assertj-core:3.15.0");

            dependencySet("org.junit.jupiter:5.6.1") {
                entry("junit-jupiter-api")
                entry("junit-jupiter-engine")
            }
        }
    }

    //configure<JavaPluginConvention> {
    //    sourceCompatibility = JavaVersion.VERSION_11
    //    targetCompatibility = JavaVersion.VERSION_11
    //}

    tasks {
        withType<JavaCompile>() {
            println("Configuring $name in project ${project.name}...")
            options.isDeprecation = true
            options.encoding = "UTF-8"
        }
        // compileJava.options.encoding = "UTF-8"
        // compileTestJava.options.encoding = "UTF-8"
        test {
            useJUnitPlatform()

            testLogging {
                events("PASSED", "FAILED", "SKIPPED")
                showStandardStreams = true
            }
        }
    }
}
