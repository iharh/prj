scalaVersion := "2.11.8"

crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")
testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oDF")
//testOptions in Test += ...
// > test-only org.acme.RedSuite -- -oD

libraryDependencies ++= Seq(
    //"org.apache.commons" % "commons-lang3"      % "3.4",

    "org.slf4j"          % "slf4j-api"          % "1.7.21",
    "org.slf4j"          % "slf4j-log4j12"      % "1.7.21",
    "log4j"              % "log4j"              % "1.2.17",

    "org.scalatest"      % "scalatest_2.11"     % "2.2.6" % "test"
)
