import org.apache.commons.compress.archivers.ArchiveEntry
import org.apache.commons.compress.archivers.ArchiveInputStream
import org.apache.commons.compress.archivers.ArchiveStreamFactory
import org.apache.commons.compress.utils.IOUtils

import org.apache.commons.lang3.SystemUtils

import org.apache.commons.io.FileUtils

import java.io.File
import java.io.InputStream

fun mySuperFun() {
    println("mySuperFun called!!!")
    println("host name: " + SystemUtils.OS_NAME)
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

fun doBench(dirLP: String) {
    val dirBuild = "${dirLP}/.build"
    val dirTarget = "${dirBuild}/target"
    val dirTargetData = "${dirTarget}/data"

    val dirDataSetPerf = "${dirLP}/datasets/Performance"

    cleanupDir(dirTargetData)

    //val fileNameArchivedDataSet = "${dirDataSetPerf}/10000 files of 2-3 Kb.zip"
    val fileNameArchivedDataSet = "${dirDataSetPerf}/100f.zip"
    unzipTo(fileNameArchivedDataSet, dirTargetData)

    // echo file="${fxlib.dir}/log4j.xml"
    val dirFxLib = "${dirTarget}/lib"
/*
    <target name="-init-fx" depends="-init-contrib">
        <delete dir="${target.dir}" />
        <mkdir dir="${fxlib.dir}" />

        <ivy:resolve file="${common.basedir}/ivy.xml" conf="fx" />
        <ivy:retrieve pattern="${fxlib.dir}/[originalname](.[ext])" conf="fx" type="jar,dll" />
    </target>
*/
}
