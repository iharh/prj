import org.apache.tools.ant.filters.ReplaceTokens

dependencies {
    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("com.google.guava:guava")

    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.boot:spring-boot-starter-logging")

    implementation("com.amazonaws:aws-java-sdk-core");
    implementation("com.amazonaws:aws-java-sdk-s3");

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
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
