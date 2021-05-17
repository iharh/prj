plugins {
    id("ivy-publish")
}

val nexusUsername: String by project
val nexusPassword: String by project

version = "1.1-SNAPSHOT"

publishing {
    publications {
        create<IvyPublication>("ivy") {
            // groupId = "org.gradle.sample"
            // artifactId = "library"
            // version = "1.1"

            from(components.getByName("java"))
            //artifact(file("$rootDir/p4/build/ivy/commons-codec-1.7.jar"))
            //artifact(file("$rootDir/p4/build/ivy/commons-cli-1.4.jar"))
        }
    }
    repositories {
        ivy {
            url = uri("http://localhost:8081/nexus/content/repositories/snapshots/")
            credentials {
                username = nexusUsername
                password = nexusPassword
            }
            isAllowInsecureProtocol = true
        }
    }
}
