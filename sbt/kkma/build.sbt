//name := "kkma"

version := "1.0-patch1"

// scalaVersion := "2.10.3"

autoScalaLibrary := false

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")

//logLevel := Level.Debug

// http://www.scala-sbt.org/release/docs/Howto/package.html
packageOptions in (Compile, packageBin) += Package.ManifestAttributes(java.util.jar.Attributes.Name.CLASS_PATH -> "org.snu.ids.ha.jar" )

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"        % "1.7.5"   % "test",
    "org.slf4j"                 % "slf4j-log4j12"    % "1.7.5"   % "test",
    "log4j"                     % "log4j"            % "1.2.17"  % "test",
    //
    "com.novocode"              % "junit-interface"  % "0.10"    % "test",
    "junit"                     % "junit"            % "4.11"    % "test",
    "org.hamcrest"              % "hamcrest-library" % "1.3"     % "test"
)
