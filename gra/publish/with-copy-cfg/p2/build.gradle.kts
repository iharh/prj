val multiPrjCfg by configurations.creating

dependencies {
    multiPrjCfg(project(":p1"))
}

tasks {
    create<Copy>("copyMultiPrj") {
        from(multiPrjCfg)
        into("$buildDir/lib.multi")
        include("*.jar")
    }
}
