plugins {
    id("com.example.java-library")

    id("ivy-publish")
}

dependencies {
    // implementation(platform("com.example.platform:product-platform"))

    implementation("commons-cli:commons-cli")
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

            from(components.getByName("java"))
        }
    }
}
