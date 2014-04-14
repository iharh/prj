//name := "l1"

//version := "1.0-SNAPSHOT"

autoScalaLibrary := false

crossPaths := false

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")

//logLevel := Level.Debug
//Warn Info

libraryDependencies ++= Seq(
  "org.elasticsearch" % "elasticsearch" % "1.0.0",
  //"org.apache.lucene" % "lucene-core"   % "4.6.1",
  // transitive dep on
  // httpcore-4.3
  // commons-logging-1.1.3
  "org.slf4j"                 % "slf4j-api"      % "1.7.5",
  "org.slf4j"                 % "slf4j-log4j12"  % "1.7.5",
  "log4j"                     % "log4j"          % "1.2.17",
  //"org.scalatest"      % "scalatest_2.10"   % "1.9.1"   % "test",
  //"commons-io"                % "commons-io"         % "2.4"     % "test"
  "com.novocode"              % "junit-interface"    % "0.10"    % "test",
  "junit"                     % "junit"              % "4.11"    % "test",
  "org.hamcrest"              % "hamcrest-library"   % "1.3"     % "test"
)

