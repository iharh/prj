//version := "0.1-SNAPSHOT"

// found by dependencyUpdates
scalaVersion := "2.11.8"

//autoScalaLibrary := false

//crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oDF")

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"      % "1.7.21",
    "org.slf4j"                 % "slf4j-log4j12"  % "1.7.21",
    "log4j"                     % "log4j"          % "1.2.17",

    "org.scalatest"             % "scalatest_2.11"     % "2.2.6"   % "test"
    //"org.scalacheck"              %% "scalacheck"      % "1.12.5"
)

