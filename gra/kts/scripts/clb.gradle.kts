import java.util.Properties

val clbCfg by tasks.creating {
    doLast {
	val instFolder = "D:/clb/inst"
	val propFolder = "${instFolder}/configurer" // "${rootProject.projectDir}"
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
