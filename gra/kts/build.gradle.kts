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
        .filter { !it.isDirectory() && it.toString().endsWith(".gradle.kts") && !it.toString().endsWith(".swp") }
        .forEach {
            from(it.toString())
        }
}

val hello by tasks.creating {
    doLast {
	println(listOf("hello", "world"))
        println("host name: " + SystemUtils.OS_NAME)
    }
}

declareMyTask()
