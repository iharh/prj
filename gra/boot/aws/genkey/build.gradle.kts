import org.apache.tools.ant.filters.ReplaceTokens

val springCloudVersion = "Hoxton.RELEASE"
val awsJavaSdkVersion = "1.11.700"

plugins {
    java
    idea
    id("org.springframework.boot") version "2.2.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
}

repositories {
    mavenCentral()
    jcenter()
}

dependencies {
    implementation("com.google.guava:guava:28.0-jre")

    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    compileOnly("org.projectlombok:lombok")

    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.boot:spring-boot-starter-logging")

    implementation("com.amazonaws:aws-java-sdk-core");
    implementation("com.amazonaws:aws-java-sdk-s3");

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.1")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion") {
            // bomProperty("spring-security-oauth2-autoconfigure.version", "2.2.1.RELEASE")
        }
    }
    dependencies {
        dependency("org.projectlombok:lombok:1.18.10")
        dependency("com.amazonaws:aws-java-sdk-core:$awsJavaSdkVersion");
        dependency("com.amazonaws:aws-java-sdk-s3:$awsJavaSdkVersion");
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

tasks {
    withType<JavaCompile> {
        options.isDeprecation = true
    }
    withType<ProcessResources> {
        filesMatching("/**/bootstrap.yml") {
            val filterTokens = mapOf(Pair("version", project.version))
            filter<ReplaceTokens>("tokens" to filterTokens)
        }
    }
    getByName("compileJava").dependsOn("processResources")
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
}
