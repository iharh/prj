import org.apache.commons.lang3.SystemUtils

scalaVersion := "2.12.1"

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions  ++= Seq("-encoding", "UTF-8", "-Xlint:deprecation", "-Xlint:unchecked")

testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oDF")

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"       % "1.7.22",
    //"org.slf4j"                 % "slf4j-log4j12"   % "1.7.21",
    //"log4j"                     % "log4j"           % "1.2.17",
    "ch.qos.logback"            % "logback-classic"   % "1.1.8",
    "ch.qos.logback"            % "logback-core"   % "1.1.8",
    "org.codehaus.groovy"       % "groovy"         % "2.4.7",

    "org.apache.commons"        % "commons-lang3"   % "3.5",

    // TODO: try only monix-reactive
    "io.monix"                  %% "monix"          % "2.1.2",
    "org.scalatest"             %% "scalatest"      % "3.0.1"   % "test"
)

val libfx = if (SystemUtils.IS_OS_LINUX)
    "/data/wrk/clb/lib.fx" else
    "D:/clb/inst/server/lib.fx"

unmanagedJars in Compile ++= {
    (file(libfx) ** "org.snu.ids.ha-patch1.jar").classpath
}
