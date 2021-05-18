ant.importBuild("$projectDir/build.xml") { antTargetName -> "ant-target-" + antTargetName }

ant.lifecycleLogLevel = org.gradle.api.AntBuilder.AntMessagePriority.INFO

val localNexusUsername: String by project
val localNexusPassword: String by project
val localNexusRealm: String by project
val localNexusHost: String by project
val localNexusRepoPublishSnapshots: String by project

tasks {
    create("antPublishNexus") {
        doLast {
            ant.setProperty("nexus.deploy.user", localNexusUsername)
            ant.setProperty("nexus.deploy.password", localNexusPassword)
            ant.setProperty("nexus.realm", localNexusRealm)
            ant.setProperty("nexus.host", localNexusHost)
            ant.setProperty("nexus.repo.snapshots", localNexusRepoPublishSnapshots)
            // ant.project.executeTarget("dump-pub-cfg")
            ant.project.executeTarget("pub-to-nexus")
        }
    }
}
