import org.apache.tools.ant.filters.ReplaceTokens
import org.gradle.internal.os.OperatingSystem

val springCloudVersion = "Hoxton.RELEASE"

val osName = if (OperatingSystem.current().isMacOsX) "macosx" else "linux"

plugins {
    java
    idea
    distribution
    id("org.springframework.boot") version "2.2.2.RELEASE"
    id("io.spring.dependency-management") version "1.0.8.RELEASE"
    id("com.github.ManifestClasspath") version "0.1.0-RELEASE"
}

repositories {
    mavenCentral()
    jcenter()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

idea {
    //module {
    //    sourceDirs.plusAssign(file("${protobuf.protobuf.generatedFilesBaseDir}/main/java"))
    //}
}

distributions {
    getByName("main") {
        distributionBaseName.set(project.name) // was baseName
        contents {
            from(tasks["jar"])
        }
    }
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
    withType<Tar> {
        enabled = false
    }
    test {
        testLogging {
            events("PASSED", "FAILED", "SKIPPED")
            showStandardStreams = false
        }
    }

    getByName("compileJava").dependsOn("processResources")
}

dependencies {
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    compileOnly("org.projectlombok:lombok")
    
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-integration")
    implementation("org.springframework.boot:spring-boot-starter-web")

    implementation("org.springframework.cloud:spring-cloud-loadbalancer")
    //implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client") {
    //    exclude("org.springframework.cloud", "spring-cloud-starter-netflix-ribbon")
    //    exclude("org.springframework.cloud", "spring-cloud-netflix-ribbon")
    //    exclude("com.netflix.ribbon", "ribbon-eureka")
    //}
    implementation("org.springframework.boot:spring-boot-starter-amqp")
    implementation("org.springframework.cloud:spring-cloud-stream")
    implementation("org.springframework.cloud:spring-cloud-stream-binder-rabbit")

    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
    implementation("org.springframework.cloud:spring-cloud-starter-zipkin")

    implementation("io.springfox:springfox-swagger2")
    implementation("io.springfox:springfox-swagger-ui")
    implementation("io.springfox:springfox-bean-validators")

    implementation("io.micrometer:micrometer-core")
    implementation("io.micrometer:micrometer-registry-prometheus")
    implementation("io.github.mweirauch:micrometer-jvm-extras")

    implementation("net.logstash.logback:logstash-logback-encoder")

    implementation("javax.xml.bind:jaxb-api")
    implementation("com.sun.xml.bind:jaxb-impl")
    implementation("com.sun.xml.bind:jaxb-core")

    
    implementation("com.google.guava:guava:28.0-jre")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-test-autoconfigure")
    testImplementation("org.springframework.amqp:spring-rabbit-test")
    testImplementation("org.springframework.cloud:spring-cloud-stream-test-support")
    testImplementation("com.github.tomakehurst:wiremock")
    
    // testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.1")
    // testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.1")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:$springCloudVersion") {
            bomProperty("spring-security-oauth2-autoconfigure.version", "2.2.1.RELEASE")
        }
    }
    dependencies {
        dependency("org.projectlombok:lombok:1.18.10")

        dependency("io.springfox:springfox-swagger2:2.9.2")
        dependency("io.springfox:springfox-swagger-ui:2.9.2")
        dependency("io.springfox:springfox-bean-validators:2.9.2")

        dependency("io.github.mweirauch:micrometer-jvm-extras:0.2.0")

        dependency("net.logstash.logback:logstash-logback-encoder:6.2")

        dependency("javax.xml.bind:jaxb-api:2.3.1")
        dependency("com.sun.xml.bind:jaxb-impl:2.3.1")
        dependency("com.sun.xml.bind:jaxb-core:2.3.0")

        dependency("com.github.tomakehurst:wiremock:2.25.1")
    }
}

// val test by tasks.getting(Test::class) {
//    useJUnitPlatform()
//}
