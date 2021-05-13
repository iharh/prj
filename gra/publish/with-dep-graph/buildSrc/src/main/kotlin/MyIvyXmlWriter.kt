import org.gradle.api.Action

import org.gradle.internal.xml.SimpleXmlWriter
import org.gradle.internal.xml.XmlTransformer

import java.io.File
import java.io.IOException
import java.io.UncheckedIOException
import java.io.Writer

class MyIvyXmlWriter {
    private val ivyFileEncoding = "UTF-8"
    private val xmlTransformer = XmlTransformer()

    // https://github.com/JetBrains/gradle-intellij-plugin/blob/master/src/main/kotlin/org/jetbrains/intellij/IntelliJIvyDescriptorFileGenerator.kt

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

    fun writeTo(file: File) {
        xmlTransformer.transform(file, ivyFileEncoding, GeneratorAction())
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
