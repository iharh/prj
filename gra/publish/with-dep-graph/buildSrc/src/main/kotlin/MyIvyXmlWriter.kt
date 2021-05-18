import org.gradle.api.Action
import org.gradle.api.artifacts.ModuleVersionIdentifier
import org.gradle.api.artifacts.ResolvedArtifact

import org.gradle.internal.xml.SimpleXmlWriter
import org.gradle.internal.xml.XmlTransformer

import java.io.File
import java.io.IOException
import java.io.UncheckedIOException
import java.io.Writer
import java.text.SimpleDateFormat
import java.util.Date

class MyIvyXmlWriter {
    private val ivyFileEncoding = "UTF-8"
    private val ivyDatePattern = "yyyyMMddHHmmss"

    private val ivyDateFormat = SimpleDateFormat(ivyDatePattern)

    private val xmlTransformer = XmlTransformer()

    private val resolvedArtifacts: MutableList<ResolvedArtifact> = ArrayList<ResolvedArtifact>()

    fun addArtifact(resolvedArtifact: ResolvedArtifact) {
        resolvedArtifacts.add(resolvedArtifact)
    }

    fun writeTo(file: File) {
        xmlTransformer.transform(file, ivyFileEncoding, GeneratorAction())
    }

    private fun writeInfo(xmlWriter : OptionalAttributeXmlWriter) {
        xmlWriter.startElement("info")
            .attribute("organisation", "mycompany")
            .attribute("module"     , "mymodule")
            .attribute("revision"   , "1.154.0-SNAPSHOT")
            .attribute("status"     , "integration")
            .attribute("publication", ivyDateFormat.format(Date()))

        xmlWriter.startElement("ivyauthor")
            .attribute("name", "my.author")
            .attribute("url", "www.my-company.com")
        xmlWriter.endElement() // "ivyauthor"

        xmlWriter.startElement("description")
            .attribute("homepage", "http://www.my-company.com")
        xmlWriter.endElement() // "description"

        xmlWriter.endElement() // "info"
    }

    private fun writeConfigurations(xmlWriter : OptionalAttributeXmlWriter) {
        xmlWriter.startElement("configurations")

        xmlWriter.startElement("conf")
            .attribute("name", "publish")
        xmlWriter.endElement() // "conf"

        xmlWriter.endElement() // "configurations"
    }

    private fun writePublications(xmlWriter : OptionalAttributeXmlWriter) {
        xmlWriter.startElement("publications")
            .attribute("defaultconf", "publish")

        resolvedArtifacts.forEach { result: ResolvedArtifact ->
            val mvId: ModuleVersionIdentifier = result.getModuleVersion().getId()
            val mvidVer = mvId.getVersion()
            val artifactVer = if (mvidVer == "unspecified") "" else "-${mvidVer}"

            var ivyArtifactName = "${result.getName()}${artifactVer}"

            xmlWriter.startElement("artifact")
                .attribute("name", ivyArtifactName)
                .attribute("type", result.getExtension())
            xmlWriter.endElement() // "artifact"
        }

        xmlWriter.endElement() // "publications"
    }

    private fun writeDependencies(xmlWriter : OptionalAttributeXmlWriter) {
        xmlWriter.startElement("dependencies")
        xmlWriter.endElement() // "dependencies"
    }

    @Throws(IOException::class)
    private fun writeDescriptor(writer: Writer) {
        val xmlWriter = OptionalAttributeXmlWriter(writer, "  ", ivyFileEncoding)
        xmlWriter.startElement("ivy-module").attribute("version", "2.0")

        writeInfo(xmlWriter)
        writeConfigurations(xmlWriter)
        writePublications(xmlWriter)
        writeDependencies(xmlWriter)

        xmlWriter.endElement() // "ivy-module"
    }

    // ??? org.gradle.kotlin.dsl.ActionExtensions.kt
    inner class GeneratorAction : Action<Writer> {
        override fun execute(writer: Writer) {
            try {
                writeDescriptor(writer)
            } catch (e: IOException) {
                throw UncheckedIOException(e)
            }
        }
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
