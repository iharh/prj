import org.apache.commons.lang3.SystemUtils

//version := "0.1-SNAPSHOT"

scalaVersion := "2.12.1"

//autoScalaLibrary := false

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")
testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oDF")
//testOptions in Test += ...
// > test-only org.acme.RedSuite -- -oD

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"      % "1.7.22",
    "org.slf4j"                 % "slf4j-log4j12"  % "1.7.22",
    "log4j"                     % "log4j"          % "1.2.17",

    "com.typesafe"              % "config"         % "1.3.1",
    //
    "org.scalatest"            %% "scalatest"      % "3.0.1"   % "test"
)

val cfgcfg = if (SystemUtils.IS_OS_LINUX)
    "/data/wrk/notes/wrk/config" else
    "D:/dev/notes/wrk/config"

unmanagedClasspath in Test += file(cfgcfg)
