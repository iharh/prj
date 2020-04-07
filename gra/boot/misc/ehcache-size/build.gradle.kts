dependencies {
    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springframework.boot:spring-boot-autoconfigure")
    // implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-cache")

    implementation("org.springframework:spring-context-support")

    implementation("javax.cache:cache-api")
    implementation("org.ehcache:ehcache")

    testImplementation("org.assertj:assertj-core")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-test-autoconfigure")

    // testImplementation("org.junit.jupiter:junit-jupiter-api")
    // testImplementation("org.junit.jupiter:junit-jupiter-engine")
}
