import org.gradle.internal.os.OperatingSystem

val localNexusUsername: String by project
val localNexusPassword: String by project
val localNexusRealm: String by project
val localNexusHost: String by project
val localNexusRepoPublishSnapshots: String by project

val pubJarCfg by configurations.creating

val pubLibCfg by configurations.creating {
    attributes {
        // need release variant for misspell compilation
        // attribute(CppBinary.OPTIMIZED_ATTRIBUTE, true)
        attribute(CppBinary.OPTIMIZED_ATTRIBUTE, false)
        // TODO: both Debug and Release variants has true value here
        // attribute(CppBinary.DEBUGGABLE_ATTRIBUTE, false)
        attribute(Usage.USAGE_ATTRIBUTE, namedAttribute(Usage.NATIVE_RUNTIME)) // NATIVE_RUNTIME NATIVE_LINK
    }
}

val sharedLibSuffix = OperatingSystem.current().getSharedLibrarySuffix()

val ivyPublishDir = "$buildDir/ivy"

ant.importBuild("$projectDir/build.xml") { antTargetName -> "ant-target-" + antTargetName }

ant.lifecycleLogLevel = org.gradle.api.AntBuilder.AntMessagePriority.INFO

dependencies {
    pubJarCfg(project(":p1"))
    pubJarCfg(project(":p2"))

    pubLibCfg(project(":pnative"))
}

tasks {
    create<Copy>("copyJars") {
        from(pubJarCfg)
        into(ivyPublishDir)
        include("*.jar")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
    create<Copy>("copySharedLibs") {
        from(pubLibCfg)
        into(ivyPublishDir)
        include("*$sharedLibSuffix")
    }
    create("ivyDescr") {
        doLast {
            val ivyXmlWriter = MyIvyXmlWriter()

            val resolvedArtifacts: Set<ResolvedArtifact> = pubJarCfg.getResolvedConfiguration().getResolvedArtifacts()
            resolvedArtifacts.forEach { result: ResolvedArtifact ->
                ivyXmlWriter.addArtifact(result)
            }

            val ivyDescriptorFileName = "$ivyPublishDir/ivy.xml"
            ivyXmlWriter.writeTo(file(ivyDescriptorFileName))
            logger.quiet("generated: $ivyDescriptorFileName")
        }
    }
    create("antPublishNexus") {
        dependsOn("copyJars", "ivyDescr")
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
