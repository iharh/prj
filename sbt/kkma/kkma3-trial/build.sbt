//name := "kkma-t"

version := "0.1-SNAPSHOT"

// scalaVersion := "2.10.3"

autoScalaLibrary := false

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")

//logLevel := Level.Debug

// http://www.scala-sbt.org/release/docs/Howto/package.html
packageOptions in (Compile, packageBin) += Package.ManifestAttributes(java.util.jar.Attributes.Name.CLASS_PATH -> "org.snu.ids.ha3-trial.jar" )

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"      % "1.7.5",
    "org.slf4j"                 % "slf4j-log4j12"  % "1.7.5",
    "log4j"                     % "log4j"          % "1.2.17",
    // one-jar stuff
    "commons-net"               % "commons-net"    % "3.3",
    //
    "com.novocode"              % "junit-interface"    % "0.10"    % "test",
    "junit"                     % "junit"              % "4.12"    % "test",
    "org.hamcrest"              % "java-hamcrest"      % "2.0.0.0" % "test"
)
