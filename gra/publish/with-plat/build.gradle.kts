plugins {
    // id("org.springframework.boot").version("2.2.11.RELEASE").apply(false)

    //id("ivy-publish")
}

allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    //apply(plugin = "java-library") // clb-java-library

    //dependencies {
    //    implementation(platform("com.example.platform:product-platform"))
    //}
}

/*
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

            from(project(":p1").components.getByName("java"))
        }
    }
}
*/
