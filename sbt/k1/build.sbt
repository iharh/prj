//name := "k1"

version := "0.2-SNAPSHOT"

scalaVersion := "2.10.3"

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")

// one-jar stuff
seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

//logLevel := Level.Debug

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"      % "1.7.5",
    "org.slf4j"                 % "slf4j-log4j12"  % "1.7.5",
    "log4j"                     % "log4j"          % "1.2.17"
    // one-jar stuff
    //"commons-lang"              % "commons-lang"   % "2.6"
    //
    //"com.novocode"              % "junit-interface"    % "0.10"    % "test",
    //"junit"                     % "junit"              % "4.11"    % "test",
)
