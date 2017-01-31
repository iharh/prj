import org.apache.commons.lang3.SystemUtils

scalaVersion := "2.12.1"

//crossPaths := false

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")
testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oDF")

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"           % "1.7.22",
    "org.slf4j"                 % "slf4j-log4j12"       % "1.7.22",
    "log4j"                     % "log4j"               % "1.2.17",
    //"org.apache.commons"        % "commons-compress"    % "1.13",

    "org.deeplearning4j"        % "deeplearning4j-nlp"  % "0.7.2" excludeAll(
        ExclusionRule(organization = "io.dropwizard"),
        ExclusionRule(organization = "ch.qos.logback"),
        ExclusionRule(organization = "com.typesafe.akka")
    ),
    "com.jsuereth"              %% "scala-arm"          % "2.0",
    //
    "org.scalatest"            %% "scalatest"           % "3.0.1"   % "test"
)

//libraryDependencies += "org.deeplearning4j" % "deeplearning4j-nlp" % "0.4-rc1.2" excludeAll(
//        ExclusionRule(organization = "io.dropwizard"),
//        ExclusionRule(organization = "ch.qos.logback"),
//        ExclusionRule(organization = "com.typesafe.akka")
//)
