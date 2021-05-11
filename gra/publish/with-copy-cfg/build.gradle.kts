import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    id("org.springframework.boot").version("2.2.11.RELEASE").apply(false)
    id("io.spring.dependency-management").version("1.0.11.RELEASE").apply(false)

    id("ivy-publish")
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "io.spring.dependency-management")

    the<DependencyManagementExtension>().apply {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:Hoxton.SR3") {
                bomProperty("spring-security-oauth2-autoconfigure.version", "2.2.4.RELEASE")
            }
        }
        dependencies {
            dependency("commons-cli:commons-cli:1.4")
        }
    }

    apply(plugin = "java-library") // clb-java-library
}

publishing {
    repositories {
        ivy {
            url = uri("$buildDir/repo")
        }
    }
    publications {
        create<IvyPublication>("myLocalIvy") {
            organisation = "org.gradle.sample"
            module = "project1-sample"
            revision = "1.1"
            //descriptor.status = "milestone"
            //descriptor.branch = "testing"
            // descriptor.extraInfo("http://my.namespace", "myElement", "Some value")

            from(project(":p1").components.getByName("java"))
        }
    }
}
