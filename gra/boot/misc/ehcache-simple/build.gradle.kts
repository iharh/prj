dependencies {
    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.projectlombok:lombok")
    //annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    //implementation("org.springframework.boot:spring-boot-autoconfigure")
    //implementation("org.springframework.boot:spring-boot-starter")

    // implementation("org.springframework:spring-context-support")

    implementation("org.ehcache:ehcache")

    testImplementation("org.assertj:assertj-core")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
}
