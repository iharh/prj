plugins {
    id("com.example.java-library")

    id("ivy-publish")
}

dependencies {
    // implementation(platform("com.example.platform:product-platform"))

    implementation(project(":p1"))
    implementation(project(":p2"))
}

publishing {
    repositories {
        ivy {
            url = uri("$buildDir/repo")
        }
    }
    publications {
        create<IvyPublication>("myLocalIvy") {
            from(components.getByName("java"))

            organisation = "org.gradle.sample"
            module = "my-prj-sample"
            revision = "1.1"
            //descriptor.status = "milestone"
            //descriptor.branch = "testing"
            // descriptor.extraInfo("http://my.namespace", "myElement", "Some value")
        }
    }
}
