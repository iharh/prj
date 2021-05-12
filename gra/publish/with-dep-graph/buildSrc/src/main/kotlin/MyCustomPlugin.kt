import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.ModuleVersionIdentifier
import org.gradle.api.artifacts.ResolvedArtifact

import org.gradle.internal.xml.SimpleXmlWriter
import org.gradle.internal.xml.XmlTransformer
import java.io.File
import java.io.IOException
import java.io.UncheckedIOException
import java.io.Writer

import org.gradle.kotlin.dsl.*

class MyCustomPlugin : Plugin<Project> {

    private val ivyFileEncoding = "UTF-8"
    private val xmlTransformer = XmlTransformer()

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
                    }
                }
            }
        }
    }

    // https://github.com/JetBrains/gradle-intellij-plugin/blob/master/src/main/kotlin/org/jetbrains/intellij/IntelliJIvyDescriptorFileGenerator.kt

    fun writeTo(file: File) {
        xmlTransformer.transform(file, ivyFileEncoding) { writer ->
            try {
                writeDescriptor(writer)
            } catch (e: IOException) {
                throw UncheckedIOException(e)
            }
        }
    }

    @Throws(IOException::class)
    private fun writeDescriptor(writer: Writer) {
        val xmlWriter = OptionalAttributeXmlWriter(writer, "  ", ivyFileEncoding)
        xmlWriter.startElement("ivy-module").attribute("version", "2.0")

        xmlWriter.startElement("info")
            .attribute("organisation", "my.org")
            .attribute("module", "my.module")
            .attribute("revision", "1.154.0")
        xmlWriter.endElement()

        // writeConfigurations(xmlWriter)
        // writePublications(xmlWriter)
        xmlWriter.endElement()
    }

    class OptionalAttributeXmlWriter(writer: Writer, indent: String, encoding: String) : SimpleXmlWriter(writer, indent, encoding) {

        override fun startElement(name: String?): OptionalAttributeXmlWriter {
            super.startElement(name)
            return this
        }

        override fun attribute(name: String?, value: String?): OptionalAttributeXmlWriter {
            if (value != null) {
                super.attribute(name, value)
            }
            return this
        }
    }
}
