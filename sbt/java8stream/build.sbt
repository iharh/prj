scalaVersion := "2.12.1"

crossPaths := false

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")
scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")
testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oDF")

libraryDependencies ++= Seq(
    "ch.qos.logback"                % "logback-classic"     % "1.1.9",
    "org.codehaus.groovy"           % "groovy-all"          % "2.4.8",

    "org.clapper"                   %% "grizzled-slf4j"     % "1.3.0",
    //"com.typesafe.scala-logging"    %% "scala-logging"      % "3.5.0",

    "com.novocode"                  % "junit-interface"     % "0.11"    % "test",
    "junit"                         % "junit"               % "4.12"    % "test",
    "org.hamcrest"                  % "hamcrest-library"    % "1.3"     % "test",

    "org.scalatest"                 %% "scalatest"          % "3.0.1"   % "test"
)

fork in Test := true
