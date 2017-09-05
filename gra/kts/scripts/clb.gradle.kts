//import org.apache.commons.lang3.SystemUtils

import java.io.FileInputStream
import java.io.FileOutputStream

import java.util.Properties

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath("org.apache.commons:commons-lang3:3.6")
    }
}

val clbCfg by tasks.creating {
    doLast {
        // println("java home: " + SystemUtils.getJavaHome())

	val propFileName = "${rootProject.projectDir}/a.properties"
	val propFile = File(propFileName) // propertiesFile.exists()
	val cfg = Properties();

	FileInputStream(propFile).use { propInputStream ->
	    cfg.load(propInputStream)
	    //cfg.each {
	    //   key, value -> println("${key} = ${value}") // project.ext[key] = value
	    //}
	}

	cfg.setProperty("directory.install", "D:/clb/inst");

	FileOutputStream("${rootProject.projectDir}/b.properties").use { propOutputStream ->
	    cfg.store(propOutputStream, null)
	}
    }
}
