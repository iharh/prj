import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension

repositories {
    mavenCentral()
}
plugins {
    //id("kotlin-dsl")
    `kotlin-dsl`
    id("io.spring.dependency-management").version("1.0.11.RELEASE")
}
kotlinDslPluginOptions {
    // gradle 7 does not use experimentall stuff anymore
    // experimentalWarning.set(false)
}
gradlePlugin {
    this.plugins {
        register("my-custom-plugin") {
            id = "my-custom"
            implementationClass = "MyCustomPlugin"
        }
    }
}
the<DependencyManagementExtension>().apply {
    dependencies {
        dependency("xalan:xalan:2.7.2")
        dependency("org.apache.httpcomponents:httpclient:4.5.13")
    }
}
dependencies {
    //"implementation"(group = "org.apache.httpcomponents", name = "httpclient")
    implementation("org.apache.httpcomponents:httpclient")
}
