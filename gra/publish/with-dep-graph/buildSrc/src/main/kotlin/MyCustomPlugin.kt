import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ModuleVersionIdentifier
import org.gradle.api.artifacts.ResolvedArtifact

import org.gradle.kotlin.dsl.*

class MyCustomPlugin : Plugin<Project> {

    override fun apply(project: Project): Unit = project.run {
        apply(plugin = "base")

        val pubCfg by configurations.creating

        dependencies {
            //pubCfg("org.eclipse.jgit:org.eclipse.jgit:4.9.2.201712150930-r")
            pubCfg(project(":p1"))
            pubCfg(project(":p2"))
        }

        tasks {
            create("ivyDescr") {
                doLast {
                    val resolvedArtifacts: Set<ResolvedArtifact> = pubCfg.getResolvedConfiguration().getResolvedArtifacts()
                    resolvedArtifacts.forEach { result: ResolvedArtifact ->
                        val mvId: ModuleVersionIdentifier = result.getModuleVersion().getId()
                        val mvidVer = mvId.getVersion()
                        val artifactVer = if (mvidVer == "unspecified") "" else "-${mvidVer}"

                        var artifactNode = "${result.getName()}${artifactVer}.${result.getExtension()}"
                        logger.quiet(artifactNode)

                        val ivyXmlWriter = MyIvyXmlWriter()
                        ivyXmlWriter.writeTo(file("$buildDir/ivy.xml"))
                    }
                }
            }
        }
    }
}
