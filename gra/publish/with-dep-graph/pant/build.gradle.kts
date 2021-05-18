//apply(plugin = "my-custom")

val localNexusUsername: String by project
val localNexusPassword: String by project
val localNexusRealm: String by project
val localNexusHost: String by project
val localNexusRepoPublishSnapshots: String by project

val pubCfg by configurations.creating

val ivyPublishDir = "$buildDir/ivy"

ant.importBuild("$projectDir/build.xml") { antTargetName -> "ant-target-" + antTargetName }

ant.lifecycleLogLevel = org.gradle.api.AntBuilder.AntMessagePriority.INFO

dependencies {
    pubCfg(project(":p1"))
    pubCfg(project(":p2"))
}

tasks {
    create<Copy>("copyJars") {
        from(pubCfg)
        into(ivyPublishDir)
        include("*.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    create("ivyDescr") {
        doLast {
            val ivyXmlWriter = MyIvyXmlWriter()

            val resolvedArtifacts: Set<ResolvedArtifact> = pubCfg.getResolvedConfiguration().getResolvedArtifacts()
            resolvedArtifacts.forEach { result: ResolvedArtifact ->
                ivyXmlWriter.addArtifact(result)
            }

            val ivyDescriptorFileName = "$ivyPublishDir/ivy.xml"
            ivyXmlWriter.writeTo(file(ivyDescriptorFileName))
            logger.quiet("generated: $ivyDescriptorFileName")
        }
    }
    create("antPublishNexus") {
        dependsOn("copyJars")
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
