import org.apache.commons.lang3.SystemUtils

scalaVersion := "2.11.8"

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions  ++= Seq("-encoding", "UTF-8", "-Xlint:deprecation", "-Xlint:unchecked")

testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oDF")

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"       % "1.7.21",
    //"org.slf4j"                 % "slf4j-log4j12"   % "1.7.21",
    //"log4j"                     % "log4j"           % "1.2.17",
    //"ch.qos.logback"            % "logback-core"      % "1.1.7",
    "ch.qos.logback"            % "logback-classic"   % "1.1.7",
    "ch.qos.logback"            % "logback-core"   % "1.1.7",
    "org.codehaus.groovy"       % "groovy"         % "2.4.7",

    "org.apache.commons"        % "commons-lang3"   % "3.4",

    "org.scalatest"             %% "scalatest"      % "3.0.0"   % "test"
)

//val libcb = if (SystemUtils.IS_OS_LINUX)
//    "/data/wrk/clb/lib.cb" else
//    "D:/clb/inst/server/lib.cb"

//unmanagedJars in Compile ++= {
//    (file(libcb) ** "postgresql-9*.jar").classpath
//}
