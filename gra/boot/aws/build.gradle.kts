import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    id("io.spring.dependency-management") version "1.0.8.RELEASE" apply false
    id("org.springframework.boot") version "2.2.2.RELEASE" apply false
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
    apply(plugin = "org.springframework.boot")

    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion") {
                // bomProperty("spring-security-oauth2-autoconfigure.version", "2.2.1.RELEASE")
            }
        }
        dependencies {
            dependency("org.projectlombok:lombok:1.18.10")

            dependency("com.google.guava:guava:28.0-jre")

            dependency("com.amazonaws:aws-java-sdk-core:$awsJavaSdkVersion")
            dependency("com.amazonaws:aws-java-sdk-s3:$awsJavaSdkVersion")

            dependency("org.yaml:snakeyaml:1.25")

            dependency("org.junit.jupiter:junit-jupiter-api:5.5.1")
            dependency("org.junit.jupiter:junit-jupiter-engine:5.5.1")
        }
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    
    tasks {
        withType<JavaCompile>() {
            println("Configuring $name in project ${project.name}...")
            options.isDeprecation = true
        }
    }
    tasks {
        withType<ProcessResources> {
            filesMatching("/**/bootstrap.yml") {
                val filterTokens = mapOf(Pair("version", project.version))
                filter<ReplaceTokens>("tokens" to filterTokens)
            }
        }
        getByName("compileJava").dependsOn("processResources")
    }

    val test by tasks.getting(Test::class) {
        useJUnitPlatform()
    }
}
