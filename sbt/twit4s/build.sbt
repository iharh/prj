import org.apache.commons.lang3.SystemUtils

scalaVersion := "2.11.8" // 2.12.1

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")
testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oDF")

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"      % "1.7.22",
    "org.slf4j"                 % "slf4j-log4j12"  % "1.7.22",
    "log4j"                     % "log4j"          % "1.2.17",

    "com.typesafe"              % "config"         % "1.3.1",
    "com.danielasfregola"      %% "twitter4s"      % "3.0",
    //
    "org.scalatest"            %% "scalatest"      % "3.0.1"   % "test"
)

val cfgtwitter = if (SystemUtils.IS_OS_LINUX)
    "/data/wrk/notes/wrk/config" else
    "D:/dev/notes/wrk/clb/hosts/twitter"

unmanagedClasspath in Test += file(cfgtwitter)
