import de.neuland.jade4j.Jade4J
import de.neuland.jade4j.JadeConfiguration
import de.neuland.jade4j.template.ClasspathTemplateLoader

import org.apache.commons.compress.archivers.ArchiveEntry
import org.apache.commons.compress.archivers.ArchiveInputStream
import org.apache.commons.compress.archivers.ArchiveStreamFactory
import org.apache.commons.compress.utils.IOUtils

import org.apache.commons.lang3.SystemUtils

import org.apache.commons.io.FileUtils

import java.io.File
import java.io.InputStream

import org.gradle.api.*
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.*
import org.gradle.kotlin.dsl.accessors.*

fun mySuperFun(dirReport: String) {
    println("dirReport: ${dirReport}")
    // delete(dirReport)
    println("os name: " + SystemUtils.OS_NAME)
}

fun cleanupDir(dirName: String) {
    val fileDir = File(dirName)
    FileUtils.forceMkdir(fileDir)
    FileUtils.deleteDirectory(fileDir)
    FileUtils.forceMkdir(fileDir)
}

fun ArchiveInputStream.forEachEntry(block: (entry: ArchiveEntry, stream: InputStream) -> Unit) {
    var entry: ArchiveEntry? = null
    while ({entry = this.getNextEntry(); entry }() != null) {
	block(entry as ArchiveEntry, this)
    }
}

fun unzipTo(fileNameArchive: String, dirNameTarget: String) {
    val dirTarget = File(dirNameTarget)
    File(fileNameArchive).inputStream().use { fis ->
	ArchiveStreamFactory().createArchiveInputStream(ArchiveStreamFactory.ZIP, fis).use { ais -> // ArchiveInputStream
	    ais.forEachEntry { entry, stream ->
		val entryName = entry.getName()
		// println("unpacking: ${entryName}")
		File(dirTarget, entryName).outputStream().use { outStream ->
		    IOUtils.copy(stream, outStream);  
		}
	    }
	}
    }
}

fun prepareBenchData(dirTargetData: String, fileNameArchivedDataSet: String) {
    cleanupDir(dirTargetData)
    unzipTo(fileNameArchivedDataSet, dirTargetData)
}

fun doGenLogCfg(templateName: String, dirFxLib: String, dirReport: String) {
    val datasetName = "datasetname"

    val loader = ClasspathTemplateLoader()
    val cfg = JadeConfiguration()
    cfg.setTemplateLoader(loader)
    cfg.setMode(Jade4J.Mode.XML)
    cfg.setPrettyPrint(true)

    val template = cfg.getTemplate(templateName)

    //val model = emptyMap<String, Object>()
    val model = mapOf(
	"dirReport" to dirReport,
	"metric" to "time",
	"dataset" to datasetName,
	"logLevel" to "TSTAT"
    )

    File("${dirFxLib}/log4j.xml").bufferedWriter().use { writer ->
	writer.write("<!DOCTYPE log4j:configuration SYSTEM \"log4j.dtd\">")
	cfg.renderTemplate(template, model, writer)
    }
}
