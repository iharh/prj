import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    id("org.springframework.boot").version("2.2.11.RELEASE").apply(false)
    id("io.spring.dependency-management").version("1.0.11.RELEASE").apply(false)
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "io.spring.dependency-management")

    val isCppSharedLibProject = project.name in listOf("pnative")

    if (isCppSharedLibProject) {
        apply(plugin = "cpp-library")
    } else {
        apply(plugin = "java-library") // clb-java-library

        the<DependencyManagementExtension>().apply {
            imports {
                mavenBom("org.springframework.cloud:spring-cloud-dependencies:Hoxton.SR3") {
                    bomProperty("spring-security-oauth2-autoconfigure.version", "2.2.4.RELEASE")
                }
            }
            dependencies {
                dependencySet("org.springframework.boot:2.2.13.RELEASE") {
                    entry("spring-boot-configuration-processor")
                    entry("spring-boot-starter-actuator")
                    entry("spring-boot-starter-amqp")
                    entry("spring-boot-starter-cache")
                    entry("spring-boot-starter-jetty")
                    entry("spring-boot-starter-security")
                    entry("spring-boot-starter-test") {
                        exclude("org.junit.vintage:junit-vintage-engine")
                    }
                    entry("spring-boot-starter-web")
                }

                dependencySet("org.springframework:5.2.11.RELEASE") {
                    entry("spring-core")
                    entry("spring-context")
                    entry("spring-aop")
                    entry("spring-beans")
                    entry("spring-expression")
                    entry("spring-jcl")
                    entry("spring-context-support")
                    entry("spring-messaging")
                    entry("spring-tx")
                    entry("spring-web")
                    entry("spring-webmvc")
                }

                dependency("commons-cli:commons-cli:1.4")
                dependency("commons-codec:commons-codec:1.7")
            }
        }
    }
}
