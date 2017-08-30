import org.apache.commons.lang3.SystemUtils

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath("org.apache.commons:commons-lang3:3.6")
    }
}

apply {
    File("scripts").walk()
        .filter { !it.isDirectory() }
        .forEach {
            from(it.toString()) // println("including " + it.toString())
        }
}

task("hello") {
    doLast {
        println("host name: " + SystemUtils.OS_NAME)
        //println("host name: " + SystemUtils.getHostName())
        println("java home: " + SystemUtils.getJavaHome())
    }
}
