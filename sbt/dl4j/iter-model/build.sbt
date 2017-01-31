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
    "org.springframework"       % "spring-core"         % "4.3.6.RELEASE",
    //"org.apache.commons"        % "commons-compress"    % "1.13",

    "org.deeplearning4j"        % "deeplearning4j-nlp"  % "0.7.2" excludeAll(
        ExclusionRule(organization = "io.dropwizard"),
        ExclusionRule(organization = "ch.qos.logback"),
        ExclusionRule(organization = "com.typesafe.akka")
    ),
    //"org.nd4j"                  % "nd4j-netlib-blas"    % "0.4-rc1.2" excludeAll(
    //"org.nd4j" % "nd4j-jblas" % "0.4-rc3.6" excludeAll(
    //"org.nd4j" % "nd4j-x86" % "0.4-rc3.8" excludeAll(
    //"org.nd4j" % "nd4j-netlib-blas" % "0.4-rc3.6" excludeAll(
    "org.nd4j"                  % "nd4j-native"         % "0.7.2",
    "org.nd4j"                  % "nd4j-native"         % "0.7.2" classifier "linux-x86_64",
    //excludeAll(
    //    ExclusionRule(organization = "io.dropwizard"),
    //    ExclusionRule(organization = "ch.qos.logback"),
    //    ExclusionRule(organization = "com.typesafe.akka")
    //),
    "com.jsuereth"              %% "scala-arm"          % "2.0",
    //
    "org.scalatest"            %% "scalatest"           % "3.0.1"   % "test"
)

fork in Test := true
