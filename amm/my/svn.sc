import $ivy.`com.typesafe:config:1.3.1`
//import $ivy.`com.trilead:trilead-ssh2:1.0.0-build221`
import $ivy.`com.trilead:trilead-ssh2:1.0.0-build221`
//import $ivy.`org.tmatesoft.svnkit:svnkit:1.8.14`
//https://mvnrepository.com/artifact/com.trilead/trilead-ssh2/1.0.0-build221
//https://github.com/lihaoyi/Ammonite/blob/master/amm/src/test/scala/ammonite/session/ProjectTests.scala
//import Resolvers._
//val oss = Resolver.Http(
//  "ambiata-oss",
//  "https://ambiata-oss.s3-ap-southeast-2.amazonaws.com",
//   IvyPattern,
//   false
// )
// interp.resolvers() = interp.resolvers() :+ oss

import com.typesafe.config.ConfigFactory
import com.typesafe.config.Config

import java.io.File

val cfgFileName = "/data/wrk/clb/hosts/svn/main.properties"
val cfgFile = new File(cfgFileName)
val cfg = ConfigFactory.parseFile(cfgFile)


