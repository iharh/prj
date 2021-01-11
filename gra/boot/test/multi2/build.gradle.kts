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
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")

    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:Hoxton.SR3") {
                bomProperty("spring-security-oauth2-autoconfigure.version", "2.2.4.RELEASE")
            }
        }
        dependencies {
            dependency("org.projectlombok:lombok:1.18.12")

            dependency("org.yaml:snakeyaml:1.26")

            dependency("org.slf4j:slf4j-api:1.7.30")

            dependency("org.osgi:org.osgi.service.component.annotations:1.3.0")

            dependency("javax.annotation:javax.annotation-api:1.3.2")

            dependencySet("org.springframework.boot:2.2.11.RELEASE") {
                entry("spring-boot-configuration-processor")
                entry("spring-boot-starter-actuator")
                entry("spring-boot-starter-amqp")
                entry("spring-boot-starter-cache")
                entry("spring-boot-starter-jetty")
                entry("spring-boot-starter-security")
                entry("spring-boot-starter-test")
                entry("spring-boot-starter-web")
            }

            dependencySet("org.springframework:5.2.11.RELEASE") {
                entry("spring-core")
                entry("spring-context")
            }

            dependencySet("org.apache.logging.log4j:2.13.3") {
                entry("log4j-api")
                entry("log4j-core")
                entry("log4j-slf4j-impl")
            }

            dependency("org.assertj:assertj-core:3.16.1")

            // upgrading to 5.6.2 leads to tests skipping
            dependencySet("org.junit.jupiter:5.5.2") {
                entry("junit-jupiter-api")
                entry("junit-jupiter-engine")
            }
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
/*
subprojects {
    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:Hoxton.SR3") {
                bomProperty("spring-security-oauth2-autoconfigure.version", "2.2.4.RELEASE")
            }
        }
    }
}
*/
