repositories {
    jcenter()
}
dependencies {
    compile("org.apache.commons:commons-lang3:3.7")
    compile("commons-io:commons-io:2.6")
    compile("org.apache.commons:commons-compress:1.15")
    compile("de.neuland-bfi:jade4j:1.2.7")

    compile("de.mpicbg.scicomp:krangl:0.6")
}
plugins {
    `kotlin-dsl`
}
