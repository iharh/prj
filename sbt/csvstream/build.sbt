name := "csvstream"

version := "1.0-SNAPSHOT"

scalaVersion := "2.10.3"

scalacOptions ++= Seq("-encoding", "UTF-8", "-Xlint","-deprecation", "-unchecked")

javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:unchecked", "-Xlint:deprecation")

testOptions += Tests.Argument(TestFrameworks.JUnit, "-q", "-a")

libraryDependencies ++= Seq(
    "org.slf4j"                 % "slf4j-api"      % "1.7.18",
    "org.slf4j"                 % "slf4j-log4j12"  % "1.7.18",
    "log4j"                     % "log4j"          % "1.2.17",

    "io.reactivex"            % "rxjava-string"      % "1.0.1",

    "net.sourceforge.javacsv" % "javacsv"          % "2.0",

    //"org.scalatest"           % "scalatest_2.10"   % "1.9.1"   % "test",
    "net.sourceforge.reb4j"   % "net.sourceforge.reb4j" % "2.1.0"  % "test",

    "com.novocode"              % "junit-interface"    % "0.11"    % "test",
    "junit"                     % "junit"              % "4.12"    % "test",
    "org.hamcrest"              % "hamcrest-library"   % "1.3"     % "test"
)
