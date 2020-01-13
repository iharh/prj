dependencies {
    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.boot:spring-boot-starter-logging")

    implementation("org.springframework.cloud:spring-cloud-starter-aws")
    // implementation("org.springframework.cloud:spring-cloud-aws-autoconfigure")

    implementation("com.google.guava:guava")
    // because we don't have spring-boot-starter-web
    implementation("org.yaml:snakeyaml") 

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}
