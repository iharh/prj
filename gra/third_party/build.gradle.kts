plugins {
    id("de.undercouch.download").version("4.0.4").apply(false)
}

allprojects {
    repositories {
        mavenCentral()
        jcenter()
    }
}

subprojects {
    apply(plugin = "de.undercouch.download")
}
