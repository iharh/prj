//name := "jsonlib"

//version := "0.1-SNAPSHOT"

// scalaVersion := "2.10.3"

autoScalaLibrary := false

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint", "-deprecation", "-unchecked", "-Xfatal-warnings")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")

parallelExecution in Test := false

//logLevel := Level.Debug

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"      % "1.7.5",
    "org.slf4j"                 % "slf4j-log4j12"  % "1.7.5",
    "log4j"                     % "log4j"          % "1.2.17",
    "org.apache.poi"            % "poi"            % "3.11",
    "org.apache.poi"            % "poi-ooxml"      % "3.11",
    // testing stuff
    "com.novocode"              % "junit-interface"    % "0.10"    % "test",
    "junit"                     % "junit"              % "4.12"    % "test"
    //"org.hamcrest"              % "java-hamcrest"   % "2.0.0.0"     % "test"
)
