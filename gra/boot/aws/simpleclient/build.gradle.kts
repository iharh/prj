dependencies {
    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    implementation("com.google.guava:guava")

    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.boot:spring-boot-starter-logging")

    implementation("com.amazonaws:aws-java-sdk-core")
    implementation("com.amazonaws:aws-java-sdk-s3")

    // because we don't have spring-boot-starter-web
    implementation("org.yaml:snakeyaml") 

    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

fun Project.declareCustomBootRunTasks(profileNames: Collection<String>) {
    profileNames.forEach {
        //println("registring task: boot${it.capitalize()}")
        tasks.register("boot${it.capitalize()}") {
            group = "application"
            description = "Runs this project as a Spring Boot application with the ${it} profile activated"
            doFirst {
                tasks.bootRun.configure {
                    systemProperty("spring.profiles.active", "aws," + it)
                }
            }
            finalizedBy("bootRun")
        }
    }
}

declareCustomBootRunTasks(listOf("minio", "dev"))
