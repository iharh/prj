import java.io.FileInputStream
import java.util.Properties

val libProps = Properties()
libProps.load(FileInputStream(file("${rootProject.getRootDir()}/lib.properties")))
val versionAbc = libProps.getProperty("abc.version")

tasks {
    create("t1") {
        doLast {
            println("versionAbc: $versionAbc")
        }
    }
}
