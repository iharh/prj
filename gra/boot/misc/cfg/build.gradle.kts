dependencies {
    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.boot:spring-boot-starter")

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
tasks.bootRun {
    // systemProperty("spring.profiles.active", "aws," + it)
    jvmArgs = listOf("-Dlang.id=en")
}
