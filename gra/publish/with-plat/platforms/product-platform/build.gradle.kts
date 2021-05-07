plugins {
    id("java-platform")
}

group = "com.example.platform"

// allow the definition of dependencies to other platforms like the Spring Boot BOM
javaPlatform.allowDependencies()

dependencies {
    api(platform("org.springframework.cloud:spring-cloud-dependencies:Hoxton.SR3"))
    // api(platform("org.springframework.boot:spring-boot-dependencies:2.4.0"))
    api("commons-cli:commons-cli:1.4")
    runtime("commons-cli:commons-cli:1.4")
    constraints {
        api("commons-cli:commons-cli:1.4")
        runtime("commons-cli:commons-cli:1.4")
    }
}
