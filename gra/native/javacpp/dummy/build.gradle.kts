import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

plugins {
    id("idea")
    id("io.spring.dependency-management").version("1.0.9.RELEASE").apply(false)
}

allprojects {
    repositories {
        mavenCentral()
        jcenter()
    }
}

subprojects {
    val verJavacpp: String by extra

    val rootProjectRootDir = rootProject.getRootDir()
    val topRootDir = "$rootProjectRootDir/.."

    val javaProviderPrj = project(":javaprovider")
    val javacppGenDir = "${javaProviderPrj.buildDir}/javacpp"
    extra["javacppGenDir"] = javacppGenDir
    // TODO: maybe remove
    // extra["javacppGenFile"] = file(javacppGenDir)
    extra["topRootDir"] = topRootDir

    apply(plugin = "io.spring.dependency-management")

    the<DependencyManagementExtension>().apply {
        dependencies {
            dependency("org.projectlombok:lombok:1.18.10")

            dependency("com.google.guava:guava:28.0-jre")

            dependency("org.bytedeco:javacpp:$verJavacpp")

            dependency("org.assertj:assertj-core:3.15.0")
            
            dependencySet("org.junit.jupiter:5.5.1") {
                entry("junit-jupiter-api")
                entry("junit-jupiter-engine")
            }
        }
    }
/*
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
*/
}
