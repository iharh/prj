import org.apache.tools.ant.filters.ReplaceTokens

val springCloudVersion = "Hoxton.RELEASE"

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

    // implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security.oauth.boot:spring-security-oauth2-autoconfigure")

    // ???
    //implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("org.apache.httpcomponents:httpclient:4.5.10")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.1")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion") {
            // bomProperty("spring-security-oauth2-autoconfigure.version", "2.2.1.RELEASE")
        }
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
