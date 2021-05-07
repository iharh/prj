plugins {
    id("java-platform")
}

group = "com.example.platform"

dependencies {
    constraints {
        api("org.springframework.boot:org.springframework.boot.gradle.plugin:2.4.0")
    }
}
