plugins {
    id("maven-publish")
}

val nexusUsername: String by project
val nexusPassword: String by project

version = "1.1-SNAPSHOT"

publishing {
    publications {
        create<MavenPublication>("maven") {
            // groupId = "org.gradle.sample"
            // artifactId = "library"
            // version = "1.1"

            from(components.getByName("java"))
        }
    }
    repositories {
        maven {
            url = uri("http://localhost:8081/nexus/content/repositories/snapshots/")
            credentials {
                username = nexusUsername
                password = nexusPassword
            }
            isAllowInsecureProtocol = true
        }
    }
}
