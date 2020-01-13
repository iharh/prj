import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    id("io.spring.dependency-management") version "1.0.8.RELEASE" apply false
}

allprojects {
    repositories {
        mavenCentral()
        jcenter()
    }
}

subprojects {
    val springCloudVersion = "Hoxton.RELEASE"
    val awsJavaSdkVersion = "1.11.700"

    apply(plugin = "java")
    apply(plugin = "idea")
    apply(plugin = "io.spring.dependency-management")

    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion") {
                // bomProperty("spring-security-oauth2-autoconfigure.version", "2.2.1.RELEASE")
            }
        }
        dependencies {
            dependency("org.projectlombok:lombok:1.18.10")
            dependency("com.amazonaws:aws-java-sdk-core:$awsJavaSdkVersion");
            dependency("com.amazonaws:aws-java-sdk-s3:$awsJavaSdkVersion");
        }
    }
}
