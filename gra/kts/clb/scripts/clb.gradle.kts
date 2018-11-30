import org.apache.commons.lang3.SystemUtils

import java.util.Properties

val clbCfg by tasks.creating {
    doLast {
        // is it possible to do this with val ?
        var instFolder = "" 
        var fxsvcHost = ""
        if (SystemUtils.IS_OS_LINUX) {
            instFolder = "/data/wrk/clb/inst"
            fxsvcHost = "localhost"

        } else {
            instFolder = "D:/clb/inst"
            fxsvcHost = "192.168.235.101"
        }

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
        listOf("ar", "bn", "de", "en", "es", "fr", "hi", "it", "pt").forEach {
            serviceCustomProps.setProperty("feign.fxservice.${it}.host", "http://${fxsvcHost}:8080")
        }
        propServiceCustomFile.outputStream().use { serviceCustomProps.store(it, null) }
    }
}
