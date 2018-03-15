import org.apache.commons.lang3.SystemUtils

import java.util.Properties

val clbCfg by tasks.creating {
    doLast {
	val winInstFolder = "D:/clb/inst"
	val linInstFolder = "/data/wrk/clb/inst"

	val instFolder = if (SystemUtils.IS_OS_LINUX) linInstFolder else winInstFolder

	val propFolder = "${instFolder}/configurer"
        val serverFolder = "${instFolder}/server"
        val tempFolder = "${serverFolder}/temp"

	val propConfigurerCmpFileName = "${propFolder}/configurer-cmp.properties" 
	val propConfigurerCmpFile = File(propConfigurerCmpFileName) // propConfigurerCmpFile.exists()
	val configurerCmpProps = Properties()
	propConfigurerCmpFile.inputStream().use { configurerCmpProps.load(it) }
	configurerCmpProps.setProperty("directory.install", instFolder)

	val tempConfigurerCmpFile = File("${propFolder}/temp-configurer-cmp.properties")
	tempConfigurerCmpFile.outputStream().use { configurerCmpProps.store(it, null) }
	tempConfigurerCmpFile.copyTo(propConfigurerCmpFile, true)
	tempConfigurerCmpFile.delete()

        val serverConfFolder = "${serverFolder}/conf"
	val propServiceCustomFileName = "${serverConfFolder}/properties-service-custom.properties" 
	val propServiceCustomFile = File(propServiceCustomFileName)
	val serviceCustomProps = Properties()
	serviceCustomProps.setProperty("ehcache.cache.name", "ehcache.dev")
	serviceCustomProps.setProperty("ehcache.alerts.cache.name", "ehcache-alerts.dev")
	propServiceCustomFile.outputStream().use { serviceCustomProps.store(it, null) }
    }
}
