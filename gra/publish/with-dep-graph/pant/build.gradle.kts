ant.importBuild("$projectDir/build.xml") { antTargetName -> "ant-target-" + antTargetName }

ant.lifecycleLogLevel = org.gradle.api.AntBuilder.AntMessagePriority.INFO

tasks {
    create("antPublishNexus") {
        doLast {
            ant.project.executeTarget("pub-to-nexus")
        }
    }
}
