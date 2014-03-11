//name := "mdmwrapper"

version := "0.1-SNAPSHOT"

// scalaVersion := "2.10.3"

autoScalaLibrary := false

crossPaths := false

//artifactName := { (sv: ScalaVersion, module: ModuleID, artifact: Artifact) =>
//  artifact.name + "-" + module.revision + "." + artifact.extension
//}

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"      % "1.7.5",
    "org.slf4j"                 % "slf4j-log4j12"  % "1.7.5",
    "log4j"                     % "log4j"          % "1.2.17"
    //
    //"com.novocode"              % "junit-interface"    % "0.10"    % "test",
    //"junit"                     % "junit"              % "4.11"    % "test",
)
