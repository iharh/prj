import org.apache.commons.lang3.SystemUtils

import java.util.Properties

val clbCfg by tasks.creating {
    doLast {
	val winInstFolder = "D:/clb/inst"
	val linInstFolder = "/data/wrk/clb/inst"

	val instFolder = if (SystemUtils.IS_OS_LINUX) linInstFolder else winInstFolder

	val propFolder = "${instFolder}/configurer"
	val propFileName = "${propFolder}/configurer-cmp.properties" 
	val propFile = File(propFileName) // propertiesFile.exists()
	val cfg = Properties()
	propFile.inputStream().use { cfg.load(it) }
	cfg.setProperty("directory.install", instFolder)
	val tmpFileName = "tmp.properties"
	val tmpFile = File("${propFolder}/${tmpFileName}")
	tmpFile.outputStream().use { cfg.store(it, null) }
	tmpFile.copyTo(propFile, true)
	tmpFile.delete()
    }
}
