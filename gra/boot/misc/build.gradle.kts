import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    val springBootVersion = "2.2.5.RELEASE"
    id("idea")
    id("io.spring.dependency-management").version("1.0.8.RELEASE").apply(false)
    id("org.springframework.boot").version(springBootVersion).apply(false)
}

allprojects {
    repositories {
        mavenCentral()
        jcenter()
    }
}

subprojects {
    val junitVersion = "5.5.1"

    apply(plugin = "java")
    apply(plugin = "idea")
    apply(plugin = "io.spring.dependency-management")
    apply(plugin = "org.springframework.boot")

    the<DependencyManagementExtension>().apply {
        dependencies {
            dependency("org.projectlombok:lombok:1.18.10")

            dependency("com.google.guava:guava:28.0-jre")

            dependency("org.yaml:snakeyaml:1.25")

            dependency("org.junit.jupiter:junit-jupiter-api:$junitVersion")
            dependency("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
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

        getting(Test::class) {
            useJUnitPlatform()
        }
    }
}
