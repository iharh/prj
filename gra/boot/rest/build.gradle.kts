import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    id("idea")
    id("io.spring.dependency-management") version "1.0.8.RELEASE" apply false
    id("org.springframework.boot") version "2.2.3.RELEASE" apply false
}

allprojects {
    repositories {
        mavenCentral()
        jcenter()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "idea")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")

    the<DependencyManagementExtension>().apply {
        dependencies {
            dependency("org.projectlombok:lombok:1.18.10")

            dependency("org.yaml:snakeyaml:1.25")

            dependency("commons-io:commons-io:2.6")
            
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

    val test by tasks.getting(Test::class) {
        useJUnitPlatform()
    }
}
