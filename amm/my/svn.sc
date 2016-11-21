import $ivy.`com.typesafe:config:1.3.1`

import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config

import java.io.File

val cfgFileName = "/data/wrk/clb/hosts/svn/main.properties"
val cfgFile = new File(cfgFileName)
val cfg = ConfigFactory.parseFile(cfgFile)
