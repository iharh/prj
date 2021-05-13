import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ResolvedArtifact
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.Copy

import org.gradle.kotlin.dsl.*

class MyCustomPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = project.run {
        // apply(plugin = "base")

        val ivyPublishDir = "$buildDir/ivy"

        val pubCfg by configurations.creating

        dependencies {
            //pubCfg("org.eclipse.jgit:org.eclipse.jgit:4.9.2.201712150930-r")
            pubCfg(project(":p1"))
            pubCfg(project(":p2"))
        }

        tasks {
            register<Copy>("ivyCopyJars") {
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
        }
    }
}
