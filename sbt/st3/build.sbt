scalaVersion := "2.11.8"

//autoScalaLibrary := false

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oDF")

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"       % "1.7.21",
    "org.slf4j"                 % "slf4j-log4j12"   % "1.7.21",
    "log4j"                     % "log4j"           % "1.2.17",

    //"org.apache.commons"        % "commons-lang3"   % "3.4",

    "org.antlr"                 % "stringtemplate"  % "3.2.1",
    // one-jar stuff
    //"commons-lang"              % "commons-lang"   % "2.6",
    //
    "org.scalatest"             %% "scalatest"      % "3.0.0"   % "test"
)
