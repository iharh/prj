import org.apache.commons.lang3.SystemUtils

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath("org.apache.commons:commons-lang3:3.6")
    }
}

task("abc") {
    doLast {
        println("host name: " + SystemUtils.OS_NAME)
    }
}
