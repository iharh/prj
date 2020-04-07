plugins {
    java
    idea
    id("org.springframework.boot") version "2.2.6.RELEASE"
    id("io.spring.dependency-management") version "1.0.9.RELEASE"
    id("com.github.ManifestClasspath") version "0.1.0-RELEASE"
    id("com.github.ben-manes.versions").version("0.28.0")
}

repositories {
    mavenCentral()
    jcenter()
}

//java {
//    sourceCompatibility = JavaVersion.VERSION_11
//    targetCompatibility = JavaVersion.VERSION_11
//}

tasks {
    withType<JavaCompile> {
        options.isDeprecation = true
    }
    test {
        useJUnitPlatform()
        
        testLogging {
            events("PASSED", "FAILED", "SKIPPED")
            showStandardStreams = true
        }
    }
}

dependencyManagement {
    dependencies {
        dependency("org.projectlombok:lombok:1.18.12")
        dependency("org.assertj:assertj-core:3.15.0");
    }
}

dependencies {
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    compileOnly("org.projectlombok:lombok")
    
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")

    testImplementation("org.assertj:assertj-core");
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-test-autoconfigure")
}
