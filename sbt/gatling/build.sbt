enablePlugins(GatlingPlugin)

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.8"
    //"org.scala-lang"            % "scala-library"  % "2.11.8",

//autoScalaLibrary := false

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-target:jvm-1.8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

//testOptions += Seq("-encoding", "UTF-8")

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"                 % "1.7.21",
    "org.slf4j"                 % "slf4j-log4j12"             % "1.7.21",
    "log4j"                     % "log4j"                     % "1.2.17",

    "io.gatling"                % "gatling-test-framework"    % "2.2.1" % "test",
    "io.gatling.highcharts"     % "gatling-charts-highcharts" % "2.2.1" % "test"
)
