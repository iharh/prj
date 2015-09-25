version := "0.1-SNAPSHOT"

autoScalaLibrary := false

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

//testOptions += Seq("-encoding", "UTF-8")
testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"      % "1.7.12",
    "org.slf4j"                 % "slf4j-log4j12"  % "1.7.12",
    "log4j"                     % "log4j"          % "1.2.17",

    "com.novocode"              % "junit-interface"    % "0.11"    % "test",
    "junit"                     % "junit"              % "4.12"    % "test",
    "org.hamcrest"              % "hamcrest-library"   % "1.3"     % "test"
)

antlr4Settings
antlr4PackageName in Antlr4 := Some("antlr4")
antlr4GenListener in Antlr4 := true
//antlr4GenVisitor in Antlr4 := true
antlr4Dependency in Antlr4 := "org.antlr" % "antlr4" % "4.5.1-1"

// antlr4:antlr4Generate
