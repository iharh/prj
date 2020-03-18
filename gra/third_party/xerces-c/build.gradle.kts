import de.undercouch.gradle.tasks.download.Download

tasks {
    val downloadZip by registering(Download::class) {
        src("https://github.com/michel-kraemer/gradle-download-task/archive/1.0.zip")
        dest(File(buildDir, "1.0.zip"))
    }

    create("abc") {
        doLast {
            println("hello from abc")
            // println("Property: ${project.extra["myNewProperty"]}") 
        }
    }
}
