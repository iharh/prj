dependencies {
    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    compileOnly("org.projectlombok:lombok")

    implementation("org.springframework.boot:spring-boot-starter-actuator")

    testImplementation("org.assertj:assertj-core");
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-test-autoconfigure")
}
/*
dependencies {
    annotationProcessor("org.projectlombok:lombok")
    compileOnly("org.projectlombok:lombok")

    implementation("org.slf4j:slf4j-api")

    compileOnly("org.osgi:org.osgi.service.component.annotations")

    // spring boot
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-jetty")

    testImplementation("org.assertj:assertj-core")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
}

tasks {
    withType<Test>().configureEach {
        useJUnitPlatform()

        testLogging {
            events("PASSED", "FAILED", "SKIPPED")
            showStandardStreams = true
        }

        systemProperties(mapOf(
            "file.encoding" to "utf-8"
        ))
    }
}
*/
